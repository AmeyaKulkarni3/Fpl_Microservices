package com.ameya.fpl.fplnominationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.ameya.fpl.fplnominationservice.exception.MatchAlreadyExistsException;
import com.ameya.fpl.fplnominationservice.model.CreateMatchNominationModel;
import com.ameya.fpl.fplnominationservice.service.MatchNominationService;
import com.ameya.fpl.fplnominationservice.utilities.Message;

@RestController
@RequestMapping("/match-nominations")
public class MatchNominationController {
	
	@Autowired
	MatchNominationService matchNominationService;
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Message> createMatch(@RequestBody CreateMatchNominationModel createMatchNomination) throws MatchAlreadyExistsException {
		
		return ResponseEntity.ok(matchNominationService.createMatch(createMatchNomination));
		
	}

}
