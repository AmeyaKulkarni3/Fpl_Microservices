package com.ameya.fpl.users.exception;

public class UserAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 696603014400519369L;
	
	public UserAlreadyExistsException() {
		super();
	}
	
	public UserAlreadyExistsException(String errors) {
		super(errors);
	}

}
