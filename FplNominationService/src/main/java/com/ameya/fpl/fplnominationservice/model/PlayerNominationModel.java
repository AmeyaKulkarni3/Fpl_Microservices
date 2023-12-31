package com.ameya.fpl.fplnominationservice.model;

import com.ameya.fpl.fplnominationservice.dto.MatchNominationDto;
import com.ameya.fpl.fplnominationservice.dto.MatchServiceResponseDto;
import com.ameya.fpl.fplnominationservice.utilities.Result;
import com.ameya.fpl.fplnominationservice.utilities.Team;

import lombok.Data;

@Data
public class PlayerNominationModel {

	private MatchNominationDto match;
	private MatchServiceResponseDto matchDetails;
	private Team nomination;
	private Result result;
	private double points;

}
