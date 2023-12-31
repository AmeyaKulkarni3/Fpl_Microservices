package com.ameya.fpl.users.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestModel {

	@NotNull(message = "Email can not be blank")
	@Email
	private String email;

	@NotNull(message = "Password can not be blank")
	@Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
	private String password;

	
}
