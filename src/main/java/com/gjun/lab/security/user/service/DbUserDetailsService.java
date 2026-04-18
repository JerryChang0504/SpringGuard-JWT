package com.gjun.lab.security.user.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gjun.lab.security.user.entity.AppUser;
import com.gjun.lab.security.user.repo.AppUserRepository;

@Service
public class DbUserDetailsService implements UserDetailsService {

	private final AppUserRepository appUserRepository;

	public DbUserDetailsService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("user not found: " + username));

		List<SimpleGrantedAuthority> authorities = user.getRoles()
				.stream()
				.map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
				.toList();

		return User.withUsername(user.getUsername())
				.password(user.getPasswordHash())
				.disabled(!user.isEnabled())
				.authorities(authorities)
				.build();
	}
}

