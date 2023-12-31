package com.ameya.fpl.fplnominationservice.exception;

public class WrongTeamSelectionExcpetion extends Exception {

	private static final long serialVersionUID = 696603014400519369L;
	
	public WrongTeamSelectionExcpetion() {
		super();
	}
	
	public WrongTeamSelectionExcpetion(String errors) {
		super(errors);
	}

}
