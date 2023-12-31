package com.ameya.fpl.users.security;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.ameya.fpl.users.dto.UserDto;
import com.ameya.fpl.users.exception.ExceptionConstants;
import com.ameya.fpl.users.exception.NoSuchUserException;
import com.ameya.fpl.users.model.LoginRequestModel;
import com.ameya.fpl.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.core.userdetails.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private Environment environment;

	@Autowired
	private UserService userService;

	public AuthenticationFilter(Environment environment, AuthenticationManager authenticationManager,
			UserService userService) {
		super(authenticationManager);
		this.environment = environment;
		this.userService = userService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String Username = ((User) authResult.getPrincipal()).getUsername();
		UserDto userDto;
		try {
			userDto = userService.getUserByEmail(Username);
		} catch (NoSuchUserException e) {
			return;
		}

		Instant now = Instant.now();
		String tokenSecret = environment.getProperty("token.secret");
		byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
//		SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SIG.HS512.toString());
		SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
		Long expTime = Long.parseLong(environment.getProperty("token.expiration-time"));

		String token = Jwts.builder().claim("scope", authResult.getAuthorities()).subject(userDto.getUserId())
				.expiration(Date.from(now.plusMillis(expTime))).issuedAt(Date.from(now)).signWith(secretKey).compact();

		response.addHeader("token", "Bearer " + token);
		response.addHeader("userId", userDto.getUserId());
		response.addHeader("roles", userDto.getRoles().toString());

	}

}
