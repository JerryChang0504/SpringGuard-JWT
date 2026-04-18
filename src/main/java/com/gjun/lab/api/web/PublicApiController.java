package com.gjun.lab.api.web;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicApiController {

	@GetMapping("/info")
	public Map<String, Object> info() {
		return Map.of("message", "公開端點：無需 JWT", "lab", "Spring Boot + JWT 分散式身分驗證");
	}
}
