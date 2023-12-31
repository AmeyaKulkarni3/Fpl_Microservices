package com.ameya.fpl.fplmatchservice.utilities;

import lombok.Data;

@Data
public class Message {

	private String message;
	
	public Message(String message) {
		this.message = message;
	}

}
