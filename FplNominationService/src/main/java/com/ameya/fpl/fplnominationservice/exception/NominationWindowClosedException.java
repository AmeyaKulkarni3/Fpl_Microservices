package com.ameya.fpl.fplnominationservice.exception;

public class NominationWindowClosedException extends Exception {

	private static final long serialVersionUID = 696603014400519369L;
	
	public NominationWindowClosedException() {
		super();
	}
	
	public NominationWindowClosedException(String errors) {
		super(errors);
	}

}
