package com.parentoff.rest.db.dao;

import com.parentoff.rest.db.MyBatisDAO;
import com.parentoff.rest.otp.model.User;

public class UserDao extends MyBatisDAO<User, String> {

	public UserDao(Class<User> type) {
		super(type);
	}

}
