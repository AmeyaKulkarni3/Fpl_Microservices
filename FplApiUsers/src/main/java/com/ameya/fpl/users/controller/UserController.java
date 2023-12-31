package com.ameya.fpl.users.controller;

import java.util.List;

import com.ameya.fpl.users.dto.UserDto;
import com.ameya.fpl.users.exception.NoSuchUserException;
import com.ameya.fpl.users.exception.UserAlreadyExistsException;
import com.ameya.fpl.users.model.CreateUserReponseModel;
import com.ameya.fpl.users.model.CreateUserRequestModel;
import com.ameya.fpl.users.model.UserResponseModel;
import com.ameya.fpl.users.service.UserService;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/status/check")
	public String status() {
		return "Working";
	}

	@PostMapping
	public ResponseEntity<CreateUserReponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) throws UserAlreadyExistsException {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserDto dto = modelMapper.map(userDetails, UserDto.class);

		return ResponseEntity.ok(userService.createUser(dto));
	}

	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN') or principal == #userId")
	public ResponseEntity<UserResponseModel> getUser(@PathVariable String userId) throws NoSuchUserException {
		UserDto userDto = userService.getUserByUserId(userId);
		UserResponseModel user = new ModelMapper().map(userDto, UserResponseModel.class);

		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return ResponseEntity.ok(userService.getAllUsers());
	}

}
