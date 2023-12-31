package com.ameya.fpl.users.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class UserDto implements Serializable {

	private static final long serialVersionUID = -5389503776031247079L;

	private String firstName;

	private String lastName;

	private String password;

	private String email;

	private String userId;

	private String encryptedPassword;
	
	private List<RoleDto> roles;
	
	

}
