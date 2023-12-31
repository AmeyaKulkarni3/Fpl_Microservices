package com.ameya.fpl.users.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.ameya.fpl.users.dto.UserDto;
import com.ameya.fpl.users.entity.AuthorityEntity;
import com.ameya.fpl.users.entity.RoleEntity;
import com.ameya.fpl.users.entity.UserEntity;
import com.ameya.fpl.users.exception.ExceptionConstants;
import com.ameya.fpl.users.exception.NoSuchUserException;
import com.ameya.fpl.users.exception.UserAlreadyExistsException;
import com.ameya.fpl.users.feign.UserResponseClient;
import com.ameya.fpl.users.model.CreateUserReponseModel;
import com.ameya.fpl.users.model.PlayerResponseModel;
import com.ameya.fpl.users.repository.RoleRepository;
import com.ameya.fpl.users.repository.UserRepository;
import com.ameya.fpl.users.service.UserService;
import com.ameya.fpl.users.utilities.Roles;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Arrays;

@Service
@PropertySource("classpath:ValidationMessages.properties")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserResponseClient playerResponse;
	
	@Autowired
	private Environment env;

	@Override
	public CreateUserReponseModel createUser(UserDto userDetails) throws UserAlreadyExistsException {
		UserEntity savedUser = userRepository.findByEmail(userDetails.getEmail());
		if (savedUser != null) {
			throw new UserAlreadyExistsException(env.getProperty(ExceptionConstants.USER_ALREADY_EXISTS.toString()));
		}
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity entity = modelMapper.map(userDetails, UserEntity.class);
		RoleEntity roleEntity = roleRepository.findByName(Roles.ROLE_USER.name());
		List<RoleEntity> roles = new ArrayList<>();
		roles.add(roleEntity);
		entity.setRoles(roles);
		entity.setPlayer(true);
		UserEntity createdUser = userRepository.save(entity);
		
		PlayerResponseModel playerRes = new PlayerResponseModel();
		playerRes.setUserId(createdUser.getUserId());
		playerRes.setFirstName(createdUser.getFirstName());
		playerRes.setLastName(createdUser.getLastName());
		
		playerResponse.createPlayer(playerRes);
		
		CreateUserReponseModel returnValue = modelMapper.map(createdUser, CreateUserReponseModel.class);

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);
		if (userEntity == null) {
			throw new UsernameNotFoundException(username);
		}
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Collection<RoleEntity> roles = userEntity.getRoles();
		roles.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			Collection<AuthorityEntity> authorityEntities = role.getAuthorities();
			authorityEntities.forEach(authEntity -> {
				authorities.add(new SimpleGrantedAuthority(authEntity.getName()));
			});
		});
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, authorities);
	}

	@Override
	public UserDto getUserByEmail(String email) throws NoSuchUserException {
		UserEntity entity = userRepository.findByEmail(email);

		if (entity == null)
			throw new NoSuchUserException(env.getProperty(ExceptionConstants.USER_NOT_FOUND.toString()));

		return new ModelMapper().map(entity, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) throws NoSuchUserException {
		UserEntity entity = userRepository.findByUserId(userId);

		if (entity == null)
			throw new NoSuchUserException(env.getProperty(ExceptionConstants.USER_NOT_FOUND.toString()));

		return new ModelMapper().map(entity, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		List<UserDto> userDtos = new ArrayList<>();
		for(UserEntity user : users) {
			UserDto dto =modelMapper.map(user, UserDto.class);
			userDtos.add(dto);
		}
		return userDtos;
	}

}
