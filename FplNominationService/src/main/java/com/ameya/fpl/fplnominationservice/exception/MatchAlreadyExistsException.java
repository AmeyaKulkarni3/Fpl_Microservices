package com.ameya.fpl.fplnominationservice.exception;

public class MatchAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -5011749846410408115L;
	
	public MatchAlreadyExistsException() {
		super();
	}
	
	public MatchAlreadyExistsException(String error) {
		super(error);
	}

}
