package com.ameya.fpl.fplnominationservice.model;

import com.ameya.fpl.fplnominationservice.dto.MatchNominationDto;
import java.util.*;

import lombok.Data;

@Data
public class MatchNominationResponseModel {
	
	private MatchNominationDto match;
	private List<MatchNominationModel> nominations;

}
