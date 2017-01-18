package com.parentoff.rest.db.dao;

import com.parentoff.rest.db.MyBatisDAO;
import com.parentoff.rest.otp.model.Otp;

/**
 * Created by pooja on 5/12/16.
 */
public class OtpDao extends MyBatisDAO<Otp, String> {

    public OtpDao(Class<Otp> type) {
        super(type);
    }
}
