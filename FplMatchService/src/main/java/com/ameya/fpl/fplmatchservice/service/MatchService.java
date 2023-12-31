package com.ameya.fpl.fplmatchservice.service;

import java.util.List;
import java.util.Map;

import com.ameya.fpl.fplmatchservice.dto.MatchDto;
import com.ameya.fpl.fplmatchservice.exception.MatchAlreadyExistsException;
import com.ameya.fpl.fplmatchservice.exception.NoSuchMatchException;
import com.ameya.fpl.fplmatchservice.exception.NoSuchTeamException;
import com.ameya.fpl.fplmatchservice.model.UpdateMatchRequestModel;
import com.ameya.fpl.fplmatchservice.utilities.Message;

public interface MatchService {

	Message createMatch(MatchDto match) throws MatchAlreadyExistsException, NoSuchTeamException;

	List<MatchDto> getAllMatches();

	List<MatchDto> getMatchesByDate(String date) throws NoSuchMatchException;

	MatchDto updateMatch(UpdateMatchRequestModel match) throws NoSuchMatchException, NoSuchTeamException;

	Message deleteMatch(int matchNumber) throws NoSuchMatchException;

	Map<Long,MatchDto> getMatchesByMatchNumbers(List<Long> matchNumbers);

	MatchDto getMatchesByMatchNumber(long matchNumber) throws NoSuchMatchException;

}
