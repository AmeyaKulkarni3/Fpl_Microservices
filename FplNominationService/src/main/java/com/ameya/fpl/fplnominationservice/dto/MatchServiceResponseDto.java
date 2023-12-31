package com.ameya.fpl.fplnominationservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class MatchServiceResponseDto {
	
	private long matchNumber;
	private LocalDate matchDate;
	private LocalTime matchTime;
	private String matchVenue;
	private String team1;
	private String team2;
	private String winner;
	private String matchType;

}
