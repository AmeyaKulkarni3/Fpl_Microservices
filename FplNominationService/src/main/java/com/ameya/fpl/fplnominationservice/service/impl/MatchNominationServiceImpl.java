package com.ameya.fpl.fplnominationservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ameya.fpl.fplnominationservice.entity.MatchNominationEntity;
import com.ameya.fpl.fplnominationservice.exception.ExceptionConstants;
import com.ameya.fpl.fplnominationservice.exception.MatchAlreadyExistsException;
import com.ameya.fpl.fplnominationservice.model.CreateMatchNominationModel;
import com.ameya.fpl.fplnominationservice.repository.MatchNominationRepository;
import com.ameya.fpl.fplnominationservice.service.MatchNominationService;
import com.ameya.fpl.fplnominationservice.utilities.Message;

@Service
public class MatchNominationServiceImpl implements MatchNominationService {
	
	@Autowired
	MatchNominationRepository matchNominationRepository;
	
	@Autowired
	Environment environment;

	@Override
	public Message createMatch(CreateMatchNominationModel matchNomination) throws MatchAlreadyExistsException {
		
		MatchNominationEntity saved = matchNominationRepository.findByMatchNumber((int)matchNomination.getMatchNumber());
		
		if(saved != null) {
			throw new MatchAlreadyExistsException(environment.getProperty(ExceptionConstants.MATCH_ALREADY_EXISTS.toString()));
		}
		
		MatchNominationEntity entity = new MatchNominationEntity();
		entity.setMatchNumber((int)matchNomination.getMatchNumber());
		entity.setMatchDate(matchNomination.getMatchDate());
		
		matchNominationRepository.save(entity);
		
		return new Message(environment.getProperty(ExceptionConstants.MATCH_CREATE_SUCCESS.toString()));
	}

}
