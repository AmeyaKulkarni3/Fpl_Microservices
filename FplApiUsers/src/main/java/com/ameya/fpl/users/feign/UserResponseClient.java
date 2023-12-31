package com.ameya.fpl.users.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ameya.fpl.users.model.PlayerResponseModel;

@FeignClient(name = "fpl-nominations")
public interface UserResponseClient {
	
	@PostMapping("/players/create")
	void createPlayer(@RequestBody PlayerResponseModel player);

}
