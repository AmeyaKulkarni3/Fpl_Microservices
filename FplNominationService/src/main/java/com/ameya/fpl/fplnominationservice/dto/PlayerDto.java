package com.ameya.fpl.fplnominationservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class PlayerDto {

	private long id;
	private String userId;
	private String firstName;
	private String lastName;
	private List<NominationDto> nominations;
	private String form;
	private double accuracy;
	private double points;

}
