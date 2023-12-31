package com.ameya.fpl.fplnominationservice.exception;

public class PlayerAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 696603014400519369L;
	
	public PlayerAlreadyExistsException() {
		super();
	}
	
	public PlayerAlreadyExistsException(String errors) {
		super(errors);
	}

}
