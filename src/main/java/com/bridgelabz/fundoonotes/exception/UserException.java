package com.bridgelabz.fundoonotes.exception;

public class UserException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserException(String message) {
		this.message = message;

	}
}
