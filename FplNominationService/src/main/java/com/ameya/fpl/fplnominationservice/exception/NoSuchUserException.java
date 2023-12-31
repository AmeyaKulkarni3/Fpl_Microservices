package com.ameya.fpl.fplnominationservice.exception;

public class NoSuchUserException extends Exception {

	private static final long serialVersionUID = 696603014400519369L;
	
	public NoSuchUserException() {
		super();
	}
	
	public NoSuchUserException(String errors) {
		super(errors);
	}

}
