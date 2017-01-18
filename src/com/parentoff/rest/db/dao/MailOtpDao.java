package com.parentoff.rest.db.dao;

import com.parentoff.rest.db.MyBatisDAO;
import com.parentoff.rest.mail_otp.model.MailOtp;

/**
 * Created by pooja on 5/12/16.
 */
public class MailOtpDao extends MyBatisDAO<MailOtp, String> {

    public MailOtpDao(Class<MailOtp> type) {
        super(type);
    }
}
