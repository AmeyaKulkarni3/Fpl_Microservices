package com.ameya.fpl.users.repository;

import com.ameya.fpl.users.entity.AuthorityEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

	AuthorityEntity findByName(String name);

}
