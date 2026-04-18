package com.gjun.lab.security.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gjun.lab.security.jwt.JwtProperties;
import com.gjun.lab.security.web.JwtAuthEntryPoint;
import com.gjun.lab.security.web.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring Security：JWT Filter、端點授權。停用預設 formLogin，避免與自訂 GET /login 頁面衝突。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter,
			JwtAuthEntryPoint jwtAuthEntryPoint) throws Exception {
		AccessDeniedHandler forbiddenJson = (HttpServletRequest request, HttpServletResponse response,
				AccessDeniedException ex) -> writeJson(response, HttpServletResponse.SC_FORBIDDEN,
						"{\"error\":\"Forbidden\",\"message\":\"Insufficient privileges\"}");

		http.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(eh -> eh.authenticationEntryPoint(jwtAuthEntryPoint).accessDeniedHandler(forbiddenJson))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.GET, "/", "/login", "/welcome", "/login-page.html").permitAll()
						.requestMatchers("/css/**", "/favicon.ico", "/error", "/v3/api-docs/**", "/swagger-ui/**",
								"/swagger-ui.html")
						.permitAll()
						.requestMatchers("/api/public/**", "/api/auth/**").permitAll()
						.requestMatchers("/api/user/**").hasRole("USER")
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	private static void writeJson(HttpServletResponse response, int status, String body) throws IOException {
		response.setStatus(status);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(body);
	}
}
