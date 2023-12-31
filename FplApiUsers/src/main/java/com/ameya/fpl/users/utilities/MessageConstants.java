package com.ameya.fpl.users.utilities;

public enum MessageConstants {
	
	ROLE_UPDATE_SUCCESSFUL("role.update.successful"),
	USER_DELETE_SUCCESSFUL("user.delete.success");
	
	private final String type;

	private MessageConstants(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
	
	
	

}
