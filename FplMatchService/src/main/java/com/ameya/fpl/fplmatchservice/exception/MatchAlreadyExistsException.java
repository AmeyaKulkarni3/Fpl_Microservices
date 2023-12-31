package com.ameya.fpl.fplmatchservice.exception;

public class MatchAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 696603014400519369L;
	
	public MatchAlreadyExistsException() {
		super();
	}
	
	public MatchAlreadyExistsException(String errors) {
		super(errors);
	}

}
