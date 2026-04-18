package com.gjun.lab.common.dto;

public record TokenResponse(
		String accessToken,
		String refreshToken,
		String tokenType,
		long expiresInSeconds) {
}
