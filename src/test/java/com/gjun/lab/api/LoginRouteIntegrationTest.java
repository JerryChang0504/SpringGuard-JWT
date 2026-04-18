package com.gjun.lab.api;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class LoginRouteIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getLoginReturnsLoginPageContent() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("身分驗證工作台")));
	}

	@Test
	void getLoginWithLogoutRedirectsWithLoggedOut() throws Exception {
		mockMvc.perform(get("/login").param("logout", ""))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login-page.html?loggedOut=1"));
	}
}
