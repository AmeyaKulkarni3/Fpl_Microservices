package com.ameya.fpl.fplmatchservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMatchRequestModel {
	
	@NotNull(message = "{match.number.must}")
	private long matchNumber;
	
	@NotNull(message = "{match.date.must}")
	private String matchDate;
	
	@NotNull(message = "{match.time.must}")
	private String matchTime;
	
	@NotNull(message = "{match.venue.must}")
	private String matchVenue;
	
	@NotNull(message = "{match.team.must}")
	private String team1;
	
	@NotNull(message = "{match.team.must}")
	private String team2;
	
	@NotNull(message = "{match.type.must}")
	private String matchType;

}
