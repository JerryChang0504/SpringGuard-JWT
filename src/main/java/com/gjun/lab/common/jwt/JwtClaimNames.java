package com.gjun.lab.common.jwt;

/**
 * JWT 自訂 Claim 名稱（對齊 Lab：簽發、校驗、refresh 與過期管理）。
 */
public final class JwtClaimNames {

	public static final String ROLES = "roles";
	public static final String TOKEN_TYPE = "typ";
	public static final String TOKEN_TYPE_REFRESH = "refresh";
	public static final String TOKEN_TYPE_ACCESS = "access";

	private JwtClaimNames() {
	}
}
