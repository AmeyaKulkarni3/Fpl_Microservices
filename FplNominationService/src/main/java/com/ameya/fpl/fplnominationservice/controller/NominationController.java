package com.ameya.fpl.fplnominationservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import com.ameya.fpl.fplnominationservice.dto.NominationDto;
import com.ameya.fpl.fplnominationservice.exception.NoSuchMatchException;
import com.ameya.fpl.fplnominationservice.exception.NoSuchTeamException;
import com.ameya.fpl.fplnominationservice.exception.NoSuchUserException;
import com.ameya.fpl.fplnominationservice.exception.NominationWindowClosedException;
import com.ameya.fpl.fplnominationservice.exception.WrongTeamSelectionExcpetion;
import com.ameya.fpl.fplnominationservice.model.CreateNominationRequestModel;
import com.ameya.fpl.fplnominationservice.service.NominationService;
import com.ameya.fpl.fplnominationservice.utilities.Message;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/nominations")
public class NominationController {

	@Autowired
	NominationService nominationService;

	@GetMapping("/status")
	public String status() {
		return "Nomination Service working";
	}

	@GetMapping("/all")
	public ResponseEntity<List<NominationDto>> getAllNominations() {
		return ResponseEntity.ok(nominationService.getAllNominations());
	}

	@PostMapping("/create")
//	@PreAuthorize("hasRole('ADMIN') or principal == #userId")
	public ResponseEntity<Message> createNomination(@Valid @RequestBody CreateNominationRequestModel nominations)
			throws NoSuchTeamException, NoSuchUserException, NoSuchMatchException, NominationWindowClosedException,
			WrongTeamSelectionExcpetion {
		return ResponseEntity.ok(nominationService.createNomination(nominations));
	}
	
	@PutMapping("/update-count")
	public ResponseEntity<Message> updateNominationsCount(){
		return ResponseEntity.ok(nominationService.updateMatchNominations());
	}

}
