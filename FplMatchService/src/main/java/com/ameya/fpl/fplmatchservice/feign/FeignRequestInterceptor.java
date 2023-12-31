package com.ameya.fpl.fplmatchservice.feign;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ameya.fpl.JwtClaimsParser;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {
	
	@Autowired
	Environment environment;
	
	@Autowired
	private HttpServletRequest request;

	@Override
	public void apply(RequestTemplate template) {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 
		 if(authentication != null) {
			 template.header(environment.getProperty("authorization.token.header.name"), 
					 request.getHeader(environment.getProperty("authorization.token.header.name")));
		 }

	}

}
