package com.ameya.fpl.fplnominationservice.exception;

public class NoSuchNominationException extends Exception {

	private static final long serialVersionUID = 696603014400519369L;
	
	public NoSuchNominationException() {
		super();
	}
	
	public NoSuchNominationException(String errors) {
		super(errors);
	}

}
