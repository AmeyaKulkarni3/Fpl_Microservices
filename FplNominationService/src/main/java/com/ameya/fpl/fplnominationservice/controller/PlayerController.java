package com.ameya.fpl.fplnominationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ameya.fpl.fplnominationservice.exception.PlayerAlreadyExistsException;
import com.ameya.fpl.fplnominationservice.model.CreatePlayerRequestModel;
import com.ameya.fpl.fplnominationservice.service.PlayerService;
import com.ameya.fpl.fplnominationservice.utilities.Message;

@RestController
@RequestMapping("/players")
public class PlayerController {
	
	@Autowired
	PlayerService playerService;
	
	@PostMapping("/create")
	public ResponseEntity<Message> createPlayer(@RequestBody CreatePlayerRequestModel player) throws PlayerAlreadyExistsException{
		
		return ResponseEntity.ok(playerService.createPlayer(player));
		
	}

}
