package com.ameya.fpl.fplmatchservice.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ameya.fpl.fplmatchservice.dto.MatchDto;
import com.ameya.fpl.fplmatchservice.exception.MatchAlreadyExistsException;
import com.ameya.fpl.fplmatchservice.exception.NoSuchMatchException;
import com.ameya.fpl.fplmatchservice.exception.NoSuchTeamException;
import com.ameya.fpl.fplmatchservice.model.CreateMatchRequestModel;
import com.ameya.fpl.fplmatchservice.model.UpdateMatchRequestModel;
import com.ameya.fpl.fplmatchservice.service.MatchService;
import com.ameya.fpl.fplmatchservice.utilities.Message;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/matches")
public class MatchController {

	@Autowired
	MatchService matchService;

	@GetMapping("/status")
	public String status() {
		return "Match Service working";
	}

	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Message> createMatch(@Valid @RequestBody CreateMatchRequestModel match)
			throws MatchAlreadyExistsException, NoSuchTeamException {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		MatchDto matchDto = modelMapper.map(match, MatchDto.class);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(match.getMatchDate(), formatter);
		matchDto.setMatchDate(date);
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H:mm");
		LocalTime time = LocalTime.parse(match.getMatchTime(), formatter1);
		matchDto.setMatchTime(time);
		return ResponseEntity.ok(matchService.createMatch(matchDto));
	}

	@GetMapping("/all")
	public ResponseEntity<List<MatchDto>> getAllMatches() {
		return ResponseEntity.ok(matchService.getAllMatches());
	}

	@GetMapping("/{date}")
	public ResponseEntity<List<MatchDto>> getMatchesByDate(@PathVariable String date) throws NoSuchMatchException {
		return ResponseEntity.ok(matchService.getMatchesByDate(date));
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MatchDto> updateMatch(@RequestBody UpdateMatchRequestModel match)
			throws NoSuchMatchException, NoSuchTeamException {

		return ResponseEntity.ok(matchService.updateMatch(match));

	}

	@DeleteMapping("/delete/{matchNumber}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Message> deleteMatch(@PathVariable int matchNumber) throws NoSuchMatchException {
		return ResponseEntity.ok(matchService.deleteMatch(matchNumber));
	}

	@GetMapping("/get-by-match-numbers")
	public ResponseEntity<Map<Long, MatchDto>> getMatchesByMatchNumbers(@RequestBody List<Long> matchNumbers) {
		return ResponseEntity.ok(matchService.getMatchesByMatchNumbers(matchNumbers));
	}

	@GetMapping("/by-match-number/{matchNumber}")
	public ResponseEntity<MatchDto> getMatchByMatchNumber(@PathVariable long matchNumber) throws NoSuchMatchException {
		MatchDto dto = matchService.getMatchesByMatchNumber(matchNumber);
		return ResponseEntity.ok(dto);
	}
}
