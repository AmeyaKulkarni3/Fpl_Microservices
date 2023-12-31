package com.ameya.fpl.fplnominationservice.exception;

public class NoSuchTeamException extends Exception {

	private static final long serialVersionUID = 696603014400519369L;
	
	public NoSuchTeamException() {
		super();
	}
	
	public NoSuchTeamException(String errors) {
		super(errors);
	}

}
