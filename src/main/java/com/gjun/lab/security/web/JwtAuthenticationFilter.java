package com.gjun.lab.security.web;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gjun.lab.security.jwt.JwtTokenService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Lab 對應「Filter Middleware」：攔截請求、解析 Authorization Bearer JWT、建立 SecurityContext。
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtTokenService jwtTokenService;

	public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
			String compact = header.substring(BEARER_PREFIX.length()).trim();
			if (StringUtils.hasText(compact)) {
				try {
					Authentication authentication = jwtTokenService.getAuthenticationFromAccessToken(compact);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
				catch (JwtException | IllegalArgumentException ex) {
					SecurityContextHolder.clearContext();
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
