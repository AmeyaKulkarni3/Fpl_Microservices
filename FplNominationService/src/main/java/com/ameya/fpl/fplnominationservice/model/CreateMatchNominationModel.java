package com.ameya.fpl.fplnominationservice.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateMatchNominationModel {
	
	private long matchNumber;
	private LocalDate matchDate;

}
