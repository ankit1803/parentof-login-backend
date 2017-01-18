package com.parentoff.rest.errorHandling;

import java.io.Serializable;

public class ParentOffException implements Serializable {
	private static final long serialVersionUID = 1L;

	Integer status;

	String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	String developerMessage;

	private String traceId;

	/**
	 * 
	 * @param status
	 * @param message
	 * @param developerMessage
	 */
	public ParentOffException(int status, String message, String developerMessage) {
		this.status = status;
		this.developerMessage = developerMessage;
		this.message = message;
	}

	public ParentOffException() {
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

}
