package com.parentoff.rest.mail_otp.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * Created by pooja on 5/12/16.
 */
public class MailOtp {
    private String app_id;
    private String user_id;
    private String email;
    private String token;
    private Date expiry_time;

    public MailOtp() {
    }

    public MailOtp(String app_id, String user_id, String email, String token, Date expiry_time) {
        this.app_id = app_id;
        this.user_id = user_id;
        this.email = email;
        this.token = token;
        this.expiry_time = expiry_time;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(Date expiry_time) {
        this.expiry_time = expiry_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
