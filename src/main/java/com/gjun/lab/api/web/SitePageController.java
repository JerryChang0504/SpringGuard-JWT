package com.gjun.lab.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SitePageController {

	@GetMapping("/")
	public String home() {
		return "home";
	}

	/**
	 * 轉發至 classpath 靜態 {@code /login-page.html}，避免 Thymeleaf 解析登入頁時與環境組合出現 500。
	 */
	@GetMapping("/login")
	public String loginPage(@RequestParam(required = false) String logout) {
		if (logout != null) {
			return "redirect:/login-page.html?loggedOut=1";
		}
		return "forward:/login-page.html";
	}

	@GetMapping("/welcome")
	public String welcomePage() {
		return "welcome";
	}
}
