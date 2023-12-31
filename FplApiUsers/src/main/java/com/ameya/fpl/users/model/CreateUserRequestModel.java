package com.ameya.fpl.users.model;

import org.springframework.context.annotation.PropertySource;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PropertySource("classpath:ValidationMessages.properties")
public class CreateUserRequestModel {

	@NotNull(message = "{user.firstname.must}")
	@Size(min = 2, message = "{user.firstname.size}")
	private String firstName;

	@NotNull(message = "{user.lastname.must}")
	@Size(min = 2, message = "{user.lastname.size}")
	private String lastName;

	@NotNull(message = "{user.password.must}")
	@Size(min = 8, max = 16, message = "{user.password.invalid}")
	private String password;

	@NotNull(message = "{user.email.must}")
	@Email
	private String email;

}
