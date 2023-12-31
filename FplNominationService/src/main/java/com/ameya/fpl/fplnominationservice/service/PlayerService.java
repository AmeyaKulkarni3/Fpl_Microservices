package com.ameya.fpl.fplnominationservice.service;

import com.ameya.fpl.fplnominationservice.exception.PlayerAlreadyExistsException;
import com.ameya.fpl.fplnominationservice.model.CreatePlayerRequestModel;
import com.ameya.fpl.fplnominationservice.utilities.Message;

public interface PlayerService {
	
	Message createPlayer(CreatePlayerRequestModel player) throws PlayerAlreadyExistsException;

}
