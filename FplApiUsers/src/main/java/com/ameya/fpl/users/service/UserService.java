package com.ameya.fpl.users.service;

import java.util.List;

import com.ameya.fpl.users.dto.UserDto;
import com.ameya.fpl.users.exception.NoSuchUserException;
import com.ameya.fpl.users.exception.UserAlreadyExistsException;
import com.ameya.fpl.users.model.CreateUserReponseModel;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	CreateUserReponseModel createUser(UserDto userDetails) throws UserAlreadyExistsException;

	UserDto getUserByEmail(String email) throws NoSuchUserException;

	UserDto getUserByUserId(String userId) throws NoSuchUserException;

	List<UserDto> getAllUsers();

}
