package com.parentoff.client.rest;

public class LoginResponse {
	String status;
	String message;
	String crmSessionId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCrmSessionId() {
		return crmSessionId;
	}

	public void setCrmSessionId(String crmSessionId) {
		this.crmSessionId = crmSessionId;
	}

	@Override
	public String toString() {
		return "LoginResponse [status=" + status + ", message=" + message
				+ ", crmSessionId=" + crmSessionId + "]";
	}

}
