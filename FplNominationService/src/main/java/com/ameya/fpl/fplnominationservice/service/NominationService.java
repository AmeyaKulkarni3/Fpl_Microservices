package com.ameya.fpl.fplnominationservice.service;

import java.util.List;

import com.ameya.fpl.fplnominationservice.dto.NominationDto;
import com.ameya.fpl.fplnominationservice.exception.NoSuchMatchException;
import com.ameya.fpl.fplnominationservice.exception.NoSuchTeamException;
import com.ameya.fpl.fplnominationservice.exception.NoSuchUserException;
import com.ameya.fpl.fplnominationservice.exception.NominationWindowClosedException;
import com.ameya.fpl.fplnominationservice.exception.WrongTeamSelectionExcpetion;
import com.ameya.fpl.fplnominationservice.model.CreateNominationRequestModel;
import com.ameya.fpl.fplnominationservice.model.MatchNominationResponseModel;
import com.ameya.fpl.fplnominationservice.model.PlayerNominationResponseModel;
import com.ameya.fpl.fplnominationservice.utilities.Message;

public interface NominationService {

	Message createNomination(CreateNominationRequestModel nomination) throws NoSuchUserException, NoSuchMatchException,
			NominationWindowClosedException, NoSuchTeamException, WrongTeamSelectionExcpetion;

	List<NominationDto> getAllNominations();

	PlayerNominationResponseModel getPlayerNominations(String userId) throws NoSuchUserException;

	MatchNominationResponseModel getMatchNominations(int matchNumber) throws NoSuchMatchException;
	
	Message updateMatchNominations();

}
