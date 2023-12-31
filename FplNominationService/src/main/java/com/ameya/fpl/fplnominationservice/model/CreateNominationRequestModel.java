package com.ameya.fpl.fplnominationservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateNominationRequestModel {
	
	@NotNull(message = "{userid.must}")
	private String userId;
	
	@NotNull(message = "{matchnumber.must}")
	private int matchNumber;
	
	@NotNull(message = "{nomination.must}")
	private String nomination;
	

}
