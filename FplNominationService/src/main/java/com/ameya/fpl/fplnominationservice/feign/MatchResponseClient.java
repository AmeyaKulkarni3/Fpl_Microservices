package com.ameya.fpl.fplnominationservice.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.ameya.fpl.fplnominationservice.dto.MatchServiceResponseDto;

@FeignClient(name = "fpl-matches")
public interface MatchResponseClient {

	@GetMapping("/matches/by-match-number/{matchNumber}")
	MatchServiceResponseDto getMatchByMatchNumber(@PathVariable long matchNumber);

	@GetMapping("/matches/get-by-match-numbers")
	Map<Long, MatchServiceResponseDto> getMatchesByMatchNumbers(@RequestBody List<Long> matchNumbers);

}
