package com.ameya.fpl.fplnominationservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ameya.fpl.fplnominationservice.entity.PlayerEntity;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long>{
	
	PlayerEntity findByUserId(String userId);

}
