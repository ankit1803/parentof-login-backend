package com.parentoff.rest.otp.resource;

import com.parentoff.client.rest.RestClient;
import com.parentoff.rest.common.GenericResponse;
import com.parentoff.rest.common.UserResponse;
import com.parentoff.rest.config.ConfigHelper;
import com.parentoff.rest.db.ParentDAO;
import com.parentoff.rest.db.dao.OtpDao;
import com.parentoff.rest.otp.model.Otp;
import com.parentoff.rest.otp.model.OtpConfirm;
import com.parentoff.rest.otp.model.User;
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
@Path("/otp")
public class OtpResource {
    public static ParentDAO<Otp, String> otpDao = new OtpDao(Otp.class);
    public static Logger LOGGER = LoggerFactory
            .getLogger(OtpResource.class);
    private Calendar calendar = Calendar.getInstance();
    private int expiryTimeInMins = Integer.parseInt(ConfigHelper.getSpecificValue("expiryTime"));
    private String smsUrl = ConfigHelper.getSpecificValue("smsUrl");
    private String smsUser = ConfigHelper.getSpecificValue("smsUser");
    private String smsPassword = ConfigHelper.getSpecificValue("smsPassword");
    private String smsSender = ConfigHelper.getSpecificValue("smsSender");
    private String priority = ConfigHelper.getSpecificValue("priority");
    private String msgType = ConfigHelper.getSpecificValue("msgType");
    private String smsTemplate = ConfigHelper.getSpecificValue("smsTemplate");

    @GET
    @Path("{user_id}/{app_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GenericResponse sentOtp(@PathParam("app_id") String app_id, @PathParam("user_id") String user_id) {
        LOGGER.info("Sending Otp");
        GenericResponse genericResponse = new GenericResponse();

        try{
            Map<String, String> context = new HashMap<>();
            context.put("app_id", app_id);
            context.put("user_id", user_id);
            Otp otp = otpDao.getOneByQueryId("getOtpById", context);
            String token;

            if(PhoneNumberUtil.isMobileNumber(user_id)){
                if(isExpiredOrNull(otp)){
                    Date expiryTime = getExpiryTime();
                    token = generateOtp();
                    Otp newOtp = new Otp(app_id, user_id, token, expiryTime);
                    LOGGER.info("New Otp : " + newOtp);
                    if(null == otp){
                        otpDao.insert(newOtp);
                    }else{
                        otpDao.update(newOtp);
                    }

                }else{
                    token = otp.getToken();
                }

                sentOtpToUser(user_id, token);
                genericResponse.setResponseCode(200);
                genericResponse.setResponseDesc("success");

            }else{
                genericResponse.setResponseCode(-1);
                genericResponse.setResponseDesc("INVALID_MOBILE_NUMBER");
            }


        }catch (Exception e){
            LOGGER.error("Exception occured in sending Otp ", e);
            genericResponse.setResponseCode(-1);
            genericResponse.setResponseDesc(e.getMessage());
        }

        return genericResponse;
    }

    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public GenericResponse validateOtp(OtpConfirm otpConfirm) {
        LOGGER.info("validating otp: " + otpConfirm);
        GenericResponse genericResponse = new GenericResponse();
        try {
            Map<String, String> context = new HashMap<>();
            context.put("app_id", otpConfirm.getApp_id());
            context.put("user_id", otpConfirm.getUser_id());

            Otp dbOtp = otpDao.getOneByQueryId("getOtpById", context);
            if(!isExpiredOrNull(dbOtp)){
                LOGGER.info("validating otp, db Otp " + dbOtp);
                if(dbOtp.getToken().equals(otpConfirm.getToken())){
                    genericResponse.setResponseCode(200);
                    genericResponse.setResponseDesc("success");
                }else{
                    genericResponse.setResponseCode(200);
                    genericResponse.setResponseDesc("invalid");
                }
            }else {
                genericResponse.setResponseCode(200);
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

    @POST
    @Path("/validate-create-user")
    @Produces(MediaType.APPLICATION_JSON)
    public UserResponse validateOtpCreateNewUser(OtpConfirm otpConfirm) {
        LOGGER.info("validating otp: " + otpConfirm);
        UserResponse userResponse = new UserResponse();
        try {
            Map<String, String> context = new HashMap<>();
            String userId = otpConfirm.getUser_id();
            context.put("app_id", otpConfirm.getApp_id());
            context.put("user_id", userId);

            Otp dbOtp = otpDao.getOneByQueryId("getOtpById", context);
            if(!isExpiredOrNull(dbOtp)){
                LOGGER.info("validating otp, db Otp " + dbOtp);
                if(dbOtp.getToken().equals(otpConfirm.getToken())){
                    UserUtil userUtil = new UserUtil();
                    User user = userUtil.getUser(userId);

                    if(null != user){
                        userResponse.setResponseCode(200);
                        userResponse.setResponseDesc("success");
                        userResponse.setData(user);
                    }else {
                        User newUser = userUtil.createUser(userId);
                        if(null != newUser){
                            userResponse.setResponseCode(200);
                            userResponse.setResponseDesc("success");
                            userResponse.setData(newUser);
                        }else{
                            userResponse.setResponseCode(-1);
                            userResponse.setResponseDesc("FAIL_TO_CREATE_NEW_USER");
                        }
                    }

                }else{
                    userResponse.setResponseCode(-1);
                    userResponse.setResponseDesc("invalid");
                }
            }else {
                userResponse.setResponseCode(-1);
                userResponse.setResponseDesc("fail");
            }


        }catch (Exception e){
            e.printStackTrace();
            userResponse.setResponseCode(-1);
            userResponse.setResponseDesc(e.getMessage());
        }
        LOGGER.info("validating otp response : " + userResponse);

        return userResponse;
    }

    private String generateOtp() {
        Random rnd = new Random();
        int n = 1000 + rnd.nextInt(9000);
        return Integer.toString(n);
    }

    private Boolean isExpiredOrNull(Otp otp){
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

    private String trimNumber(String mobile){
        String trimmedNumber = mobile;
        if(mobile != null){
            int length = mobile.length();
            if(length != 10){
                if(mobile.startsWith("91")){
                    trimmedNumber = mobile.substring(2);
                }else if(mobile.startsWith("+91")){
                    trimmedNumber = mobile.substring(3);
                }
            }
        }

        return trimmedNumber;
    }

    private void sentOtpToUser(String user_id, String token){
        String message = StringUtils.replace(smsTemplate, "#", token);
        LOGGER.info("sms to user: " + user_id + ", message: " + message);

        String trimmedNumber = trimNumber(user_id);

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("user", smsUser);
        queryMap.put("pass", smsPassword);
        queryMap.put("sender", smsSender);
        queryMap.put("phone", trimmedNumber);
        queryMap.put("text", message);
        queryMap.put("priority", priority);
        queryMap.put("stype", msgType);
        String response = RestClient.get(smsUrl, queryMap);
        LOGGER.info("sms to user: " + user_id + ", response: " + response);
    }

}
