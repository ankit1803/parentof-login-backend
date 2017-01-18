package com.parentoff.rest.otp.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by pooja on 5/12/16.
 */
public class Otp {
    private String app_id;
    private String user_id;
    private String token;
    private Date expiry_time;

    public Otp() {
    }

    public Otp(String app_id, String user_id, String token, Date expiry_time) {
        this.app_id = app_id;
        this.user_id = user_id;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
