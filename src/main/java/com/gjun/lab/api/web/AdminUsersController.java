package com.gjun.lab.api.web;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminUsersController {

	@GetMapping("/users")
	public Map<String, Object> listUsers() {
		return Map.of("users", List.of("admin", "user"));
	}
}
