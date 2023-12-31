package com.ameya.fpl.users.exception;

public enum ExceptionConstants {
	
	USER_NOT_FOUND("user.not.found"),
	GENERAL_EXCEPTION("general.exception"),
	USER_ALREADY_EXISTS("user.already.exists"),
	ROLE_ALREADY_ASSINGED("role.already.assigned"),
	ROLE_NOT_FOUND("role.not.found");

	private final String type;

	private ExceptionConstants(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
