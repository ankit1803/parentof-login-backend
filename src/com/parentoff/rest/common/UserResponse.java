package com.parentoff.rest.common;

import com.parentoff.rest.otp.model.User;

public class UserResponse extends GenericResponse{
	private User data;

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}
}
