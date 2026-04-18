package com.gjun.lab.security.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 未通過身分驗證時回傳 401 JSON（對齊 Lab：無效則拒絕存取）。
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		if (isApiRequest(request)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
			return;
		}
		String ctx = request.getContextPath();
		response.sendRedirect(ctx + "/login");
	}

	private static boolean isApiRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		String prefix = (ctx == null || ctx.isEmpty()) ? "/api" : ctx + "/api";
		return uri.startsWith(prefix);
	}
}
