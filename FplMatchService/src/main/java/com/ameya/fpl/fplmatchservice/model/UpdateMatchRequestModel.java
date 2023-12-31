package com.ameya.fpl.fplmatchservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMatchRequestModel {
	
	@NotNull(message = "{match.number.must}")
	private long matchNumber;

	private String matchDate;

	private String matchTime;

	private String matchVenue;

	private String team1;

	private String team2;

	private String matchtype;
	
	private String winner;

}
