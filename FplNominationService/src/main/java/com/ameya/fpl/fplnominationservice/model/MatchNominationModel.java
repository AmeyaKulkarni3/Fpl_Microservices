package com.ameya.fpl.fplnominationservice.model;

import com.ameya.fpl.fplnominationservice.dto.PlayerDto;
import com.ameya.fpl.fplnominationservice.utilities.Result;
import com.ameya.fpl.fplnominationservice.utilities.Team;

import lombok.Data;

@Data
public class MatchNominationModel {

	private PlayerDto player;
	private Team nomination;
	private Result result;
	private double points;

}
