package com.ameya.fpl.fplnominationservice.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ameya.fpl.fplnominationservice.entity.MatchNominationEntity;

@Repository
public interface MatchNominationRepository extends CrudRepository<MatchNominationEntity, Long> {
	
	MatchNominationEntity findByMatchNumber(int matchNumber);
	
	List<MatchNominationEntity> findMatchByMatchDate(LocalDate date);

}
