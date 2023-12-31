package com.ameya.fpl.fplmatchservice.service.impl;

import org.springframework.stereotype.Service;

import com.ameya.fpl.fplmatchservice.dto.MatchDto;
import com.ameya.fpl.fplmatchservice.entity.MatchEntity;
import com.ameya.fpl.fplmatchservice.exception.ExceptionConstants;
import com.ameya.fpl.fplmatchservice.exception.MatchAlreadyExistsException;
import com.ameya.fpl.fplmatchservice.exception.NoSuchMatchException;
import com.ameya.fpl.fplmatchservice.exception.NoSuchTeamException;
import com.ameya.fpl.fplmatchservice.feign.MatchResponseClient;
import com.ameya.fpl.fplmatchservice.model.CreateMatchNominationModel;
import com.ameya.fpl.fplmatchservice.model.UpdateMatchRequestModel;
import com.ameya.fpl.fplmatchservice.repository.MatchRepository;
import com.ameya.fpl.fplmatchservice.service.MatchService;
import com.ameya.fpl.fplmatchservice.utilities.MatchType;
import com.ameya.fpl.fplmatchservice.utilities.Message;
import com.ameya.fpl.fplmatchservice.utilities.Team;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@PropertySource("classpath:ValidationMessages.properties")
public class MatchServiceImpl implements MatchService {

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	Environment environment;
	
	@Autowired
	MatchResponseClient matchResponseClient;

	@Override
	public Message createMatch(MatchDto match) throws MatchAlreadyExistsException, NoSuchTeamException {

		MatchEntity entity = matchRepository.findByMatchNumber(match.getMatchNumber());

		if (entity != null) {
			if ((entity.getTeam1().toString().equals(match.getTeam1().toString())
					&& entity.getTeam2().toString().equals(match.getTeam2().toString()))
					|| (entity.getTeam2().toString().equals(match.getTeam1().toString())
							&& entity.getTeam1().toString().equals(match.getTeam2().toString()))) {

				if (match.getMatchType().equals(MatchType.LEAGUE.toString())) {
					throw new MatchAlreadyExistsException(
							environment.getProperty(ExceptionConstants.MATCH_ALREADY_EXISTS.toString()));
				}
			}
		} else {
			entity = new MatchEntity();
		}

		entity.setMatchNumber(match.getMatchNumber());
		entity.setMatchDate(match.getMatchDate());
		entity.setMatchTime(match.getMatchTime());
		entity.setMatchVenue(match.getMatchVenue());

		if (match.getMatchType().equals(MatchType.PLAYOFF.name())) {
			entity.setMatchType(MatchType.PLAYOFF);
		} else {
			throw new MatchAlreadyExistsException(
					environment.getProperty(ExceptionConstants.MATCH_ALREADY_EXISTS.toString()));
		}
		entity.setTeam1(getTeam(match.getTeam1()));
		entity.setTeam2(getTeam(match.getTeam2()));
		entity.setWinner(Team.TBD);
		matchRepository.save(entity);
		CreateMatchNominationModel matchNominationModel = new CreateMatchNominationModel();
		matchNominationModel.setMatchNumber(entity.getMatchNumber());
		matchNominationModel.setMatchDate(entity.getMatchDate());
		matchResponseClient.createMatch(matchNominationModel);
		return new Message(environment.getProperty(ExceptionConstants.MATCH_CREATION_SUCCESS.toString()));
	}

	private Team getTeam(String team) throws NoSuchTeamException {

		switch (team) {
			case "CSK":
				return Team.CSK;
			case "DC":
				return Team.DC;
			case "GT":
				return Team.GT;
			case "KKR":
				return Team.KKR;
			case "LSG":
				return Team.LSG;
			case "MI":
				return Team.MI;
			case "PBKS":
				return Team.PBKS;
			case "RCB":
				return Team.RCB;
			case "RR":
				return Team.RR;
			case "SRH":
				return Team.SRH;
			case "TBD":
				return Team.TBD;
			default:
				throw new NoSuchTeamException(environment.getProperty(ExceptionConstants.TEAM_NOT_FOUND.toString()));
		}
	}

	@Override
	public List<MatchDto> getAllMatches() {

		List<MatchEntity> matchEntities = (List<MatchEntity>) matchRepository.findAll();

		List<MatchDto> matches = new ArrayList<>();

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		matchEntities.forEach(match -> {
			matches.add(matchEntityToDto(match));

		});

		return matches;
	}

	@Override
	public List<MatchDto> getMatchesByDate(String date) throws NoSuchMatchException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		List<MatchEntity> matchEntities = matchRepository.findByMatchDate(localDate);
		if (matchEntities == null || matchEntities.size() == 0) {
			throw new NoSuchMatchException(environment.getProperty(ExceptionConstants.MATCH_NOT_FOUND.toString()));
		}
		List<MatchDto> matches = new ArrayList<>();
		matchEntities.forEach(match -> {
			matches.add(matchEntityToDto(match));

		});

		return matches;

	}

	private MatchDto matchEntityToDto(MatchEntity entity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		MatchDto dto = modelMapper.map(entity, MatchDto.class);
		return dto;
	}

	@Override
	public MatchDto updateMatch(UpdateMatchRequestModel match) throws NoSuchMatchException, NoSuchTeamException {
		MatchEntity entity = matchRepository.findByMatchNumber(match.getMatchNumber());
		if (entity == null) {
			throw new NoSuchMatchException(environment.getProperty(ExceptionConstants.MATCH_NOT_FOUND.toString()));
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		if (match.getMatchDate() != null) {
			LocalDate localDate = LocalDate.parse(match.getMatchDate(), formatter);
			if (!localDate.equals(entity.getMatchDate())) {
				entity.setMatchDate(localDate);
			}
		}
		if (match.getMatchTime() != null) {
			LocalTime localTime = LocalTime.parse(match.getMatchTime(), formatter);
			if (!localTime.equals(entity.getMatchTime())) {
				entity.setMatchTime(localTime);
			}
		}
		if (match.getMatchVenue() != null && !match.getMatchVenue().equals(entity.getMatchVenue())) {
			entity.setMatchVenue(match.getMatchVenue());
		}
		if (match.getTeam1() != null && !match.getTeam1().equals(entity.getTeam1().toString())) {
			entity.setTeam1(getTeam(match.getTeam1()));
		}
		if (match.getTeam2() != null && !match.getTeam2().equals(entity.getTeam2().toString())) {
			entity.setTeam2(getTeam(match.getTeam2()));
		}
		if (match.getWinner() != null && !match.getWinner().equals(entity.getWinner().toString())) {
			entity.setWinner(getTeam(match.getWinner()));
		}
		if (match.getMatchtype() != null && !entity.getMatchType().toString().equals(match.getMatchtype())) {
			entity.setMatchType(getMatchType(match.getMatchtype()));
		}
		MatchEntity saved = matchRepository.save(entity);
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		MatchDto dto = modelMapper.map(saved, MatchDto.class);

		return dto;
	}

	private MatchType getMatchType(String type) {
		switch (type) {
			case "PLAYOFF":
				return MatchType.PLAYOFF;
			case "LEAGUE":
				return MatchType.LEAGUE;
			default:
				return null;
		}
	}

	@Override
	public Message deleteMatch(int matchNumber) throws NoSuchMatchException {
		MatchEntity entity = matchRepository.findByMatchNumber(matchNumber);
		if (entity == null) {
			throw new NoSuchMatchException(environment.getProperty(ExceptionConstants.MATCH_NOT_FOUND.toString()));
		}
		matchRepository.delete(entity);
		return new Message(environment.getProperty(ExceptionConstants.MATCH_DELETE_SUCCESS.toString()));
	}

	@Override
	public Map<Long,MatchDto> getMatchesByMatchNumbers(List<Long> matchNumbers) {
		Map<Long,MatchDto> matches = new HashMap<>();
		matchNumbers.forEach(number -> {
			MatchEntity entity = matchRepository.findByMatchNumber(number);
			MatchDto mdto = matchEntityToDto(entity);
			matches.put(number, mdto);
			
		});
		return matches;
	}

	@Override
	public MatchDto getMatchesByMatchNumber(long matchNumber) throws NoSuchMatchException {
		MatchEntity entity = matchRepository.findByMatchNumber(matchNumber);
		if (entity == null) {
			throw new NoSuchMatchException(environment.getProperty(ExceptionConstants.MATCH_NOT_FOUND.toString()));
		}
		MatchDto dto = matchEntityToDto(entity);
		return dto;
	}

}
