package com.ameya.fpl.users.security;

import com.ameya.fpl.users.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurity {

	@Autowired
	private Environment environment;

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		AuthenticationFilter authenticationFilter = new AuthenticationFilter(environment, authenticationManager,
				userService);

		http.csrf(csrfConfigurer -> {
			csrfConfigurer.disable();
//			csrfConfigurer.ignoringRequestMatchers(PathRequest.toH2Console());
		});

		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/users"))
					.permitAll().anyRequest().authenticated();

		}).addFilter(new AuthorizationFilter(authenticationManager, environment)).addFilter(authenticationFilter)
				.authenticationManager(authenticationManager);

		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

		return http.build();
	}

}
