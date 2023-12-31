package com.ameya.fpl.fplmatchservice.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.ameya.fpl.fplmatchservice.utilities.MatchType;
import com.ameya.fpl.fplmatchservice.utilities.Team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "matches")
@Data
public class MatchEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, unique = true)
	private long matchNumber;
	
	@Column(nullable = false)
	private LocalDate matchDate;
	
	@Column(nullable = false)
	private LocalTime matchTime;
	
	@Column(nullable = false)
	private String matchVenue;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Team team1;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Team team2;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Team winner;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MatchType matchType;

}
