package com.ameya.fpl.fplmatchservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ameya.fpl.fplmatchservice.model.CreateMatchNominationModel;

@FeignClient(name = "fpl-nominations")
public interface MatchResponseClient {

	@PostMapping("/match-nominations/create")
	void createMatch(@RequestBody CreateMatchNominationModel matchNomination);
}
