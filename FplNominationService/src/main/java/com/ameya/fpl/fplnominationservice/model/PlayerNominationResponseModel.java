package com.ameya.fpl.fplnominationservice.model;

import com.ameya.fpl.fplnominationservice.dto.PlayerDto;
import java.util.*;

import lombok.Data;

@Data
public class PlayerNominationResponseModel {
	
	private PlayerDto player;
	private List<PlayerNominationModel> nominations;

}
