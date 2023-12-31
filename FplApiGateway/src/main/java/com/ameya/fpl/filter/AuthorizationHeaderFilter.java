package com.ameya.fpl.filter;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.net.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>{
	
	@Autowired
	Environment environment;
	
	public AuthorizationHeaderFilter() {
		super(Config.class);
	}
	
	public static class Config{
		
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange,chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange,"No Authorization Header",HttpStatus.UNAUTHORIZED);
			}
			String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authHeader.replace("Bearer ", "");
			
			if(!isJwtValid(jwt)) {
				return onError(exchange,"Token is not valid",HttpStatus.UNAUTHORIZED);
			}
			
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}
	
	private boolean isJwtValid(String jwt) {
		boolean isValid = true;
		String subject = null;
		
		String tokenSecret = environment.getProperty("token.secret");
		
		byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
		SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
		
		JwtParser jwtParser = Jwts.parser()
				.verifyWith(secretKey)
				.build();
		
		try {
			Claims claims = jwtParser.parseSignedClaims(jwt).getPayload();
			subject = claims.getSubject();
		} catch(Exception e) {
			isValid = false;
		}	
		if(subject == null || subject.isEmpty()) {
			isValid = false;
		}
		return isValid;
	}

}
