package com.ameya.fpl.fplnominationservice.service;

import com.ameya.fpl.fplnominationservice.exception.MatchAlreadyExistsException;
import com.ameya.fpl.fplnominationservice.model.CreateMatchNominationModel;
import com.ameya.fpl.fplnominationservice.utilities.Message;

public interface MatchNominationService {	
	
	Message createMatch(CreateMatchNominationModel createMatchNomination) throws MatchAlreadyExistsException;

}
