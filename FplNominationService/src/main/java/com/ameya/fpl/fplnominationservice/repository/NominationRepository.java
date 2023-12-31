package com.ameya.fpl.fplnominationservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ameya.fpl.fplnominationservice.entity.NominationEntity;

@Repository
public interface NominationRepository extends CrudRepository<NominationEntity, Long> {

}
