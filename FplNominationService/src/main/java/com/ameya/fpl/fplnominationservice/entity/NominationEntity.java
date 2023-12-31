package com.ameya.fpl.fplnominationservice.entity;

import java.io.Serializable;

import com.ameya.fpl.fplnominationservice.utilities.Result;
import com.ameya.fpl.fplnominationservice.utilities.Team;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "nominations")
@Data
public class NominationEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="user_id", unique = false)
	private PlayerEntity player;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="match_id", unique = false)
	private MatchNominationEntity match;
	
	@Enumerated(EnumType.STRING)
	private Team nomination;
	
	@Enumerated(EnumType.STRING)
	private Result result;
	
	@Column
	private double points;

}
