package com.ameya.fpl.fplnominationservice.dto;

import com.ameya.fpl.fplnominationservice.utilities.Result;
import com.ameya.fpl.fplnominationservice.utilities.Team;

import lombok.Data;

@Data
public class NominationDto {

	private long id;
	private PlayerDto player;
	private MatchNominationDto match;
	private Team nomination;
	private Result result;
	private double points;

}
