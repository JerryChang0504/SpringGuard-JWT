package com.gjun.lab.api.web;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

	@GetMapping("/profile")
	public Map<String, Object> profile(Authentication authentication) {
		String roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
		return Map.of("username", authentication.getName(), "authorities", roles);
	}
}
