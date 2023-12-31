package com.ameya.fpl.fplmatchservice.exception;

public enum ExceptionConstants {
	
	MATCH_NOT_FOUND("match.not.found"),
	GENERAL_EXCEPTION("general.exception"),
	TEAM_NOT_FOUND("team.not.found"),
	MATCH_CREATION_SUCCESS("match.creation.success"),
	MATCH_DELETE_SUCCESS("match.delete.success"),
	MATCH_ALREADY_EXISTS("match.already.exists");


	private final String type;

	private ExceptionConstants(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
