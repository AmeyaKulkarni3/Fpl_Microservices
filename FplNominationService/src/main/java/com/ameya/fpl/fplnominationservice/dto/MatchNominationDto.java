package com.ameya.fpl.fplnominationservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class MatchNominationDto {

	private long id;
	private int matchNumber;
	private boolean isNominationClosed;
	private int nominationCountTeam1;
	private int nominationCountTeam2;
	private int noNomination;
	private List<NominationDto> nominations;
	private MatchServiceResponseDto matchDetails;

}
