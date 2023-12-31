package com.ameya.fpl.fplnominationservice.exception;

public class NoSuchMatchException extends Exception {

	private static final long serialVersionUID = 696603014400519369L;
	
	public NoSuchMatchException() {
		super();
	}
	
	public NoSuchMatchException(String errors) {
		super(errors);
	}

}
