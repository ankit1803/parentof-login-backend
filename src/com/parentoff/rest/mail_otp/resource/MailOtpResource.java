package com.parentoff.rest.mail_otp.resource;

import com.parentoff.rest.common.GenericResponse;
import com.parentoff.rest.common.STATUS_DESC;
import com.parentoff.rest.config.ConfigHelper;
import com.parentoff.rest.db.ParentDAO;
import com.parentoff.rest.db.dao.MailOtpDao;
import com.parentoff.rest.mail_otp.model.MailOtp;
import com.parentoff.rest.mail_otp.model.MailOtpConfirm;
import com.parentoff.rest.otp.model.User;
import com.parentoff.rest.otp.resource.PhoneNumberUtil;
import com.parentoff.rest.otp.resource.UserUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by ankit on 10/5/16.
 */
@Path("/email-otp")
public class MailOtpResource {
    public static ParentDAO<MailOtp, String> mailOtpDao = new MailOtpDao(MailOtp.class);
    public static Logger LOGGER = LoggerFactory
            .getLogger(MailOtpResource.class);
    private Calendar calendar = Calendar.getInstance();
    private int expiryTimeInMins = Integer.parseInt(ConfigHelper.getSpecificValue("expiryTime"));
    private String smsTemplate = ConfigHelper.getSpecificValue("smsTemplate");

    @GET
    @Path("{email_id}/{app_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GenericResponse sentMailOtp(@PathParam("email_id") String email_id,
                                       @PathParam("app_id") String app_id) {

        GenericResponse genericResponse = new GenericResponse();
        UserUtil userUtil = new UserUtil();
        User user = userUtil.getUserByEmailAppId(email_id, app_id);
        if(null == user){
            genericResponse.setResponseCode(-1);
            genericResponse.setResponseDesc(STATUS_DESC.USER_NOT_FOUND_FOR_EMAIL);

        }else{
            genericResponse = sendMailWithOtp(email_id, app_id, user.getUsername());
        }
        return genericResponse;

    }

    @GET
    @Path("{email_id}/{user_id}/{app_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GenericResponse sentMailOtp(@PathParam("email_id") String email_id,
                                   @PathParam("app_id") String app_id,
                                   @PathParam("user_id") String user_id) {

        return sendMailWithOtp(email_id, app_id, user_id);

    }

    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public GenericResponse validateMailOtp(MailOtpConfirm mailOtpConfirm) {
        LOGGER.info("validating otp: " + mailOtpConfirm);
        GenericResponse genericResponse = new GenericResponse();
        try {
            Map<String, String> context = new HashMap<>();
            context.put("email", mailOtpConfirm.getEmail());
            context.put("app_id", mailOtpConfirm.getApp_id());
            context.put("user_id", mailOtpConfirm.getUser_id());

            MailOtp dbMailOtp = mailOtpDao.getOneByQueryId("getMailOtpById", context);
            if(!isExpiredOrNull(dbMailOtp)){
                LOGGER.info("validating otp, db MailOtp " + dbMailOtp);
                if(dbMailOtp.getToken().equals(mailOtpConfirm.getToken())){
                    genericResponse.setResponseCode(200);
                    genericResponse.setResponseDesc("success");
                }else{
                    genericResponse.setResponseCode(-1);
                    genericResponse.setResponseDesc("invalid");
                }
            }else {
                genericResponse.setResponseCode(-1);
                genericResponse.setResponseDesc("fail");
            }


        }catch (Exception e){
            e.printStackTrace();
            genericResponse.setResponseCode(-1);
            genericResponse.setResponseDesc(e.getMessage());
        }
        LOGGER.info("validating otp response : " + genericResponse);

        return genericResponse;
    }

    private String generateMailOtp() {
        Random rnd = new Random();
        int n = 1000 + rnd.nextInt(9000);
        return Integer.toString(n);
    }

    private Boolean isExpiredOrNull(MailOtp otp){
        LOGGER.info("isExpiredOrNull : " + otp);
        if(null == otp){
            return true;
        }
        Date otpTime = otp.getExpiry_time();

        Date now = calendar.getTime();
        return now.after(otpTime);
    }

    private Date getExpiryTime(){

        Date now = new Date();

        Timestamp currentTime = new Timestamp(now.getTime());
        Long time = currentTime.getTime();
        long expiryDiff = expiryTimeInMins * 60 * 1000;
        now.setTime(time + expiryDiff);

        return now;

    }

    private GenericResponse sendMailWithOtp(String email_id, String app_id, String user_id){
        GenericResponse genericResponse = new GenericResponse();

        try{
            Map<String, String> context = new HashMap<>();
            context.put("email", email_id);
            context.put("app_id", app_id);
            context.put("user_id", user_id);
            LOGGER.info("Sending MailOtp " + context);
            MailOtp otp = mailOtpDao.getOneByQueryId("getMailOtpById", context);

            if(!PhoneNumberUtil.isMobileNumber(user_id)){
                genericResponse.setResponseCode(-1);
                genericResponse.setResponseDesc("INVALID_MOBILE_NUMBER");
                return genericResponse;
            }

            String token;
            if(isExpiredOrNull(otp)){
                Date expiryTime = getExpiryTime();
                token = generateMailOtp();
                MailOtp newMailOtp = new MailOtp(app_id, user_id, email_id, token, expiryTime);
                LOGGER.info("New MailOtp : " + newMailOtp);
                if(null == otp){
                    mailOtpDao.insert(newMailOtp);
                }else{
                    mailOtpDao.update(newMailOtp);
                }

            }else{
                token = otp.getToken();
            }

            sentMailOtpToUser(email_id, token);
            genericResponse.setResponseCode(0);
            genericResponse.setResponseDesc("success");

        }catch (Exception e){
            LOGGER.error("Exception occured in sending MailOtp ", e);
            genericResponse.setResponseCode(-1);
            genericResponse.setResponseDesc(e.getMessage());
        }

        return genericResponse;

    }

    private void sentMailOtpToUser(String emailId, String token){
        String message = StringUtils.replace(smsTemplate, "#", token);
        EmailManager.send(emailId, message);
    }

}
