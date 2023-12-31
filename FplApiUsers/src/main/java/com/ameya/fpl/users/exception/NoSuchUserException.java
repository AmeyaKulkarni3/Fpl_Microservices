package com.ameya.fpl.users.exception;

public class NoSuchUserException extends Exception {

	private static final long serialVersionUID = 2676925725135196962L;
	
	public NoSuchUserException() {
		super();
	}
	
	public NoSuchUserException(String errors) {
		super(errors);
	}

}
