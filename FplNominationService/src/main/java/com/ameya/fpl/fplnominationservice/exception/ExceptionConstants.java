package com.ameya.fpl.fplnominationservice.exception;

public enum ExceptionConstants {
	
	GENERAL_EXCEPTION("general.exception"),
	USER_NOT_FOUND("user.not.found"),
	MATCH_NOT_FOUND("match.not.found"),
	NOMINATION_SUCCESS("nomination.success"),
	NOMINATION_NOT_FOUND("nomunation.not.found"),
	NOMINATION_WINDOW_CLOSED("nomination.window.closed"),
	NOMINATION_COUNT_UPDATED("nomination.count.updated"),
	TEAM_NOT_FOUND("team.not.found"),
	PLAYER_CREATE_SUCCESS("player.craete.success"),
	MATCH_ALREADY_EXISTS("match.already.exists"),
	MATCH_CREATE_SUCCESS("match.creation.success"),
	WRONG_TEAM_SELECTION("wrong.team.selection"),
	PLAYER_ALREADY_EXISTS("player.already.exists");

	private final String type;

	private ExceptionConstants(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
