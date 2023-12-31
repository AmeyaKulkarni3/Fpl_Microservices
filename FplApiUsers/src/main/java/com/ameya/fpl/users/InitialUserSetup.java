package com.ameya.fpl.users;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import com.ameya.fpl.users.entity.AuthorityEntity;
import com.ameya.fpl.users.entity.RoleEntity;
import com.ameya.fpl.users.entity.UserEntity;
import com.ameya.fpl.users.repository.AuthorityRepository;
import com.ameya.fpl.users.repository.RoleRepository;
import com.ameya.fpl.users.repository.UserRepository;
import com.ameya.fpl.users.utilities.Roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class InitialUserSetup {

	@Autowired
	AuthorityRepository authorityRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	@EventListener
	public void onApplicationEvent(ApplicationReadyEvent event) {

		AuthorityEntity readAuthority = createAuthority("READ");
		AuthorityEntity writeAuthority = createAuthority("WRITE");
		AuthorityEntity deleteAuthority = createAuthority("DELETE");

		RoleEntity user = createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
		RoleEntity admin = createRole(Roles.ROLE_ADMIN.name(),
				Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

		if (admin == null)
			return;

		UserEntity adminUser = new UserEntity();
		adminUser.setFirstName("admin");
		adminUser.setLastName("admin");
		adminUser.setUserId(UUID.randomUUID().toString());
		adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
		adminUser.setEmail("admin@email.com");
		adminUser.setRoles(Arrays.asList(admin));
		adminUser.setPlayer(false);

		UserEntity userEntity = userRepository.findByEmail(adminUser.getEmail());

		if (userEntity == null) {
			userRepository.save(adminUser);
		}

	}

	@Transactional
	private AuthorityEntity createAuthority(String name) {
		AuthorityEntity entity = authorityRepository.findByName(name);
		if (entity == null) {
			entity = new AuthorityEntity();
			entity.setName(name);
			authorityRepository.save(entity);
		}
		return entity;
	}

	@Transactional
	private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
		RoleEntity role = roleRepository.findByName(name);
		if (role == null) {
			role = new RoleEntity();
			role.setName(name);
			role.setAuthorities(authorities);
			roleRepository.save(role);
		}
		return role;
	}

}
