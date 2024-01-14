package com.in.nit.exception;

public class UserNotFoundException extends RuntimeException {

	String message;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String msg) {
		super(msg);
		this.message = msg;
	}
	
	
}
