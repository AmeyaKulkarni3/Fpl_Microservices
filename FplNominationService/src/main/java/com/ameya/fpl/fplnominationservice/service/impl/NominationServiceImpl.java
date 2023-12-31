package com.ameya.fpl.fplnominationservice.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ameya.fpl.fplnominationservice.dto.MatchNominationDto;
import com.ameya.fpl.fplnominationservice.dto.MatchServiceResponseDto;
import com.ameya.fpl.fplnominationservice.dto.NominationDto;
import com.ameya.fpl.fplnominationservice.dto.PlayerDto;
import com.ameya.fpl.fplnominationservice.entity.MatchNominationEntity;
import com.ameya.fpl.fplnominationservice.entity.NominationEntity;
import com.ameya.fpl.fplnominationservice.entity.PlayerEntity;
import com.ameya.fpl.fplnominationservice.exception.ExceptionConstants;
import com.ameya.fpl.fplnominationservice.exception.NoSuchMatchException;
import com.ameya.fpl.fplnominationservice.exception.NoSuchTeamException;
import com.ameya.fpl.fplnominationservice.exception.NoSuchUserException;
import com.ameya.fpl.fplnominationservice.exception.NominationWindowClosedException;
import com.ameya.fpl.fplnominationservice.exception.WrongTeamSelectionExcpetion;
import com.ameya.fpl.fplnominationservice.feign.MatchResponseClient;
import com.ameya.fpl.fplnominationservice.model.CreateNominationRequestModel;
import com.ameya.fpl.fplnominationservice.model.MatchNominationResponseModel;
import com.ameya.fpl.fplnominationservice.model.PlayerNominationModel;
import com.ameya.fpl.fplnominationservice.model.PlayerNominationResponseModel;
import com.ameya.fpl.fplnominationservice.repository.MatchNominationRepository;
import com.ameya.fpl.fplnominationservice.repository.NominationRepository;
import com.ameya.fpl.fplnominationservice.repository.PlayerRepository;
import com.ameya.fpl.fplnominationservice.service.NominationService;
import com.ameya.fpl.fplnominationservice.utilities.Message;
import com.ameya.fpl.fplnominationservice.utilities.Team;

@Service
@PropertySource("classpath:ValidationMessages.properties")
public class NominationServiceImpl implements NominationService {

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	MatchNominationRepository matchRepository;

	@Autowired
	NominationRepository nominationRepository;

	@Autowired
	MatchResponseClient matchResponseClient;

	@Autowired
	Environment environment;

	@Override
	public Message createNomination(CreateNominationRequestModel nomination) throws NoSuchUserException,
			NoSuchMatchException, NominationWindowClosedException, NoSuchTeamException, WrongTeamSelectionExcpetion {

		PlayerEntity player = playerRepository.findByUserId(nomination.getUserId());

		if (player == null) {
			throw new NoSuchUserException(environment.getProperty(ExceptionConstants.USER_NOT_FOUND.toString()));
		}

		MatchNominationEntity match = matchRepository.findByMatchNumber(nomination.getMatchNumber());

		if (match == null) {
			throw new NoSuchMatchException(environment.getProperty(ExceptionConstants.MATCH_NOT_FOUND.toString()));
		}

		if (match.isNominationClosed()) {
			throw new NominationWindowClosedException(
					environment.getProperty(ExceptionConstants.NOMINATION_WINDOW_CLOSED.toString()));
		}

		Team nom = getTeam(nomination.getNomination());

		MatchServiceResponseDto mdto = matchResponseClient.getMatchByMatchNumber(match.getMatchNumber());

		if (!mdto.getTeam1().equals(nom.name()) && !mdto.getTeam2().equals(nom.name())) {
			throw new WrongTeamSelectionExcpetion(
					environment.getProperty(ExceptionConstants.WRONG_TEAM_SELECTION.toString()));
		}

		NominationEntity nomEntity = new NominationEntity();

		nomEntity.setMatch(match);
		nomEntity.setPlayer(player);
		nomEntity.setNomination(nom);

		nominationRepository.save(nomEntity);

		return new Message(environment.getProperty(ExceptionConstants.NOMINATION_SUCCESS.toString()));
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
	public List<NominationDto> getAllNominations() {

		List<NominationEntity> nominations = (List<NominationEntity>) nominationRepository.findAll();

		List<NominationDto> nominationDtos = new ArrayList<>();

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		nominations.forEach(nomination -> {
			NominationDto ndto = modelMapper.map(nomination, NominationDto.class);
			nominationDtos.add(ndto);

		});
		return nominationDtos;
	}

	@Override
	public PlayerNominationResponseModel getPlayerNominations(String userId) throws NoSuchUserException {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

		PlayerEntity player = playerRepository.findByUserId(userId);

		if (player == null) {
			throw new NoSuchUserException(environment.getProperty(ExceptionConstants.USER_NOT_FOUND.toString()));
		}

		List<NominationEntity> nominations = player.getNominations();

		List<PlayerNominationModel> nominationRes = new ArrayList<>();

		List<Long> matchNumbers = new ArrayList<>();

		nominations.forEach(nomination -> {
			if (nomination.getMatch().isNominationClosed()) {
				matchNumbers.add((long) nomination.getMatch().getMatchNumber());
			}
		});

		Map<Long, MatchServiceResponseDto> matchDetails = matchResponseClient.getMatchesByMatchNumbers(matchNumbers);

		nominations.forEach(nomination -> {
			PlayerNominationModel res = new PlayerNominationModel();
			MatchNominationEntity match = nomination.getMatch();
			MatchNominationDto mdto = new MatchNominationDto();
			if (match.isNominationClosed()) {
				mdto.setMatchNumber(match.getMatchNumber());
				mdto.setNominationClosed(match.isNominationClosed());
				mdto.setNominationCountTeam1(match.getNominationCountTeam1());
				mdto.setNominationCountTeam2(match.getNominationCountTeam2());
				mdto.setNoNomination(match.getNoNomination());
				res.setMatch(mdto);
				res.setMatchDetails(matchDetails.get((long)match.getMatchNumber()));
				res.setNomination(nomination.getNomination());
				res.setPoints(nomination.getPoints());
				res.setResult(nomination.getResult());
			}
			nominationRes.add(res);
		});

		PlayerDto pdto = modelMapper.map(player, PlayerDto.class);
		PlayerNominationResponseModel returnValue = new PlayerNominationResponseModel();

		returnValue.setPlayer(pdto);
		returnValue.setNominations(nominationRes);

		return returnValue;
	}

	@Override
	public MatchNominationResponseModel getMatchNominations(int matchNumber) throws NoSuchMatchException {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		MatchNominationEntity match = matchRepository.findByMatchNumber(matchNumber);

		if (match == null) {
			throw new NoSuchMatchException(environment.getProperty(ExceptionConstants.MATCH_NOT_FOUND.toString()));
		}

		MatchNominationResponseModel returnValue = new MatchNominationResponseModel();

		Map<Long, MatchServiceResponseDto> matchDetails = matchResponseClient
				.getMatchesByMatchNumbers(new ArrayList<>(matchNumber));

		if (match.isNominationClosed()) {
			List<NominationEntity> nominations = match.getNominations();

			List<PlayerNominationModel> nominationRes = new ArrayList<>();

			nominations.forEach(nomination -> {
				PlayerNominationModel res = modelMapper.map(nomination, PlayerNominationModel.class);
				nominationRes.add(res);
			});

			MatchNominationDto mdto = new MatchNominationDto();
			mdto.setMatchNumber(match.getMatchNumber());
			mdto.setNominationClosed(match.isNominationClosed());
			mdto.setNominationCountTeam1(match.getNominationCountTeam1());
			mdto.setNominationCountTeam2(match.getNominationCountTeam2());
			mdto.setNoNomination(match.getNoNomination());
			mdto.setMatchDetails(matchDetails.get(matchNumber));

			returnValue.setMatch(mdto);
			returnValue.setNominations(null);
		}

		return returnValue;
	}

	@Override
	public Message updateMatchNominations() {

		long noOfPlayers = playerRepository.count();

		LocalDate today = LocalDate.now().plusDays(3);

		List<MatchNominationEntity> entities = matchRepository.findMatchByMatchDate(today);
		List<Long> matchNumbers = new ArrayList<>();
		entities.forEach(en -> {
			matchNumbers.add((long) en.getMatchNumber());
		});

		Map<Long, MatchServiceResponseDto> matchDetails = matchResponseClient.getMatchesByMatchNumbers(matchNumbers);

		entities.forEach(entity -> {
			List<NominationEntity> nominations = entity.getNominations();
			int team1Noms = 0;
			int team2Noms = 0;
			int noNoms = 0;
			MatchServiceResponseDto m = matchDetails.get((long)entity.getMatchNumber());
			for (NominationEntity nom : nominations) {
				if (m.getTeam1().equals(nom.getNomination().name())) {
					team1Noms = team1Noms + 1;
				} else if (m.getTeam2().equals(nom.getNomination().name())) {
					team2Noms = team2Noms + 1;
				}
			}
			noNoms = (int) noOfPlayers - (team1Noms + team2Noms);
			entity.setNominationClosed(true);
			entity.setNominationCountTeam1(team1Noms);
			entity.setNominationCountTeam2(team2Noms);
			entity.setNoNomination(noNoms);
			matchRepository.save(entity);
		});
		
		return new Message(environment.getProperty(ExceptionConstants.NOMINATION_COUNT_UPDATED.toString()));

	}

}
