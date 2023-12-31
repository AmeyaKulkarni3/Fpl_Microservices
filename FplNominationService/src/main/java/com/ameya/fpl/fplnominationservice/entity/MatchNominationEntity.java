package com.ameya.fpl.fplnominationservice.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="match_nominations")
@Data
public class MatchNominationEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private int matchNumber;
	
	@Column
	private LocalDate matchDate;
	
	@Column
	private boolean isNominationClosed = false;
	
	@Column
	private int nominationCountTeam1;
	
	@Column
	private int nominationCountTeam2;
	
	@Column
	private int noNomination;
	
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name="match_id")
	private List<NominationEntity> nominations;
	
}
