package com.ameya.fpl.fplmatchservice.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ameya.fpl.fplmatchservice.entity.MatchEntity;

@Repository
public interface MatchRepository extends CrudRepository<MatchEntity, Long> {

	MatchEntity findByMatchNumber(long matchNumber);
	
	List<MatchEntity> findByMatchDate(LocalDate date);

}
