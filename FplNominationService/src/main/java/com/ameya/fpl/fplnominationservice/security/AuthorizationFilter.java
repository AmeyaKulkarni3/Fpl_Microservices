package com.ameya.fpl.fplnominationservice.security;

import java.io.IOException;
import com.ameya.fpl.JwtClaimsParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	@Autowired
	private Environment environment;

	public AuthorizationFilter(AuthenticationManager authenticationManager, Environment environment) {
		super(authenticationManager);
		this.environment = environment;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String authorizationHeader = request.getHeader(environment.getProperty("authorization.token.header.name"));

		if (authorizationHeader == null
				|| !authorizationHeader.startsWith(environment.getProperty("authorization.token.header.prefix"))) {
			chain.doFilter(request, response);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(environment.getProperty("authorization.token.header.name"));
		if (authorizationHeader == null) {
			return null;
		}
		String token = authorizationHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");
		String tokenSecret = environment.getProperty("token.secret");

		if (tokenSecret == null)
			return null;

		JwtClaimsParser jwtClaimsParser = new JwtClaimsParser(token, tokenSecret);
		String userId = jwtClaimsParser.getJwtSubject();

		if (userId == null)
			return null;

		return new UsernamePasswordAuthenticationToken(userId, null, jwtClaimsParser.getUserAuthorities());
	}

}
