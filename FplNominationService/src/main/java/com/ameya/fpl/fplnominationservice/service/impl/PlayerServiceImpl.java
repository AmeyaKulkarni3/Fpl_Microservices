package com.ameya.fpl.fplnominationservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ameya.fpl.fplnominationservice.entity.PlayerEntity;
import com.ameya.fpl.fplnominationservice.exception.ExceptionConstants;
import com.ameya.fpl.fplnominationservice.exception.PlayerAlreadyExistsException;
import com.ameya.fpl.fplnominationservice.model.CreatePlayerRequestModel;
import com.ameya.fpl.fplnominationservice.repository.PlayerRepository;
import com.ameya.fpl.fplnominationservice.service.PlayerService;
import com.ameya.fpl.fplnominationservice.utilities.Message;

@Service
public class PlayerServiceImpl implements PlayerService {
	
	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	Environment environment;

	@Override
	public Message createPlayer(CreatePlayerRequestModel player) throws PlayerAlreadyExistsException {
		
		PlayerEntity entity = playerRepository.findByUserId(player.getUserId());
		
		if(entity != null) {
			throw new PlayerAlreadyExistsException(environment.getProperty(ExceptionConstants.PLAYER_ALREADY_EXISTS.toString()));
		}
		
		PlayerEntity newPlayer = new PlayerEntity();
		newPlayer.setUserId(player.getUserId());
		newPlayer.setFirstName(player.getFirstName());
		newPlayer.setLastName(player.getLastName());
		
		playerRepository.save(newPlayer);
		
		return new Message(environment.getProperty(ExceptionConstants.PLAYER_CREATE_SUCCESS.toString()));
	}

}
