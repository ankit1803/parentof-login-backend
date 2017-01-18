package com.parentoff.rest.mail_otp.resource;



import com.parentoff.rest.common.GenericResponse;
import com.parentoff.rest.config.ConfigHelper;
import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Created by pooja on 27/12/16.
 */
public class EmailManager {
    public static Logger LOGGER = LoggerFactory
            .getLogger(MailOtpResource.class);

    private static String fromMail = ConfigHelper.getSpecificValue("fromMail");
    private static String apiKey = ConfigHelper.getSpecificValue("apiKey");
    private static String subject = ConfigHelper.getSpecificValue("subject");

    public static GenericResponse send(String toMail, String body){
        GenericResponse genericResponse = new GenericResponse();

        Email from = new Email(fromMail);
        Email to = new Email(toMail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);

        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sg.api(request);
            genericResponse.setResponseCode(200);
            genericResponse.setResponseDesc("success");
            LOGGER.info("email sent to - " + toMail + ", code - " + response.statusCode + response.body);
        } catch (IOException ex) {
            genericResponse.setResponseCode(-1);
            genericResponse.setResponseDesc(ex.getMessage());
            LOGGER.error(ex.getMessage());
        }
        return genericResponse;
    }
}
