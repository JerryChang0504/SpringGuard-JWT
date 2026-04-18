package com.gjun.lab.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.gjun.lab.common.jwt.JwtClaimNames;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {

  private final JwtProperties properties;

  // 使用 SHA-256 雜湊處理 secret
  private final String SIGNING_ALGORITHM = "SHA-256";

  public JwtTokenService(JwtProperties properties) {
    this.properties = properties;
  }

  public String createAccessToken(UserDetails user) {
    // 確保 userDetails 不為 null，並且 username 和 authorities 都有值。這是生成有效 JWT 的前提。
    Assert.notNull(user, "userDetails");
    Instant now = Instant.now();
    List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    return Jwts.builder()
        .subject(user.getUsername())
        .claim(JwtClaimNames.ROLES, roles)
        .claim(JwtClaimNames.TOKEN_TYPE, JwtClaimNames.TOKEN_TYPE_ACCESS)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(properties.getAccessTokenMinutes() * 60)))
        .signWith(signingKey())
        .compact();
  }

  public String createRefreshToken(String username) {
    Assert.hasText(username, "username");
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(username)
        .claim(JwtClaimNames.TOKEN_TYPE, JwtClaimNames.TOKEN_TYPE_REFRESH)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(properties.getRefreshTokenDays() * 24 * 60 * 60)))
        .signWith(signingKey())
        .compact();
  }

  /**
   * 從 Access Token 解析出 Authentication。確保 Token 的類型正確，並且包含必要的 Claims（如 subject 和
   * roles）。如果 Token
   * 
   * @param token
   * @return
   */
  public Authentication getAuthenticationFromAccessToken(String token) {
    Claims claims = parseAccessTokenClaims(token);
    String username = claims.getSubject();
    @SuppressWarnings("unchecked")
    List<String> roles = claims.get(JwtClaimNames.ROLES, List.class);
    if (roles == null || roles.isEmpty()) {
      throw new JwtException("missing roles claim");
    }
    List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();
    return new UsernamePasswordAuthenticationToken(username, null, authorities);
  }

  /**
   * 從 Refresh Token 解析出 subject（username）。確保 Token 的類型正確，並且包含必要的 Claims（如
   * subject）。如果 Token 無效或過期，會拋出 JwtException，讓調用者能夠適當處理（如返回 401 Unauthorized）。
   * 
   * @param refreshToken
   * @return
   */
  public String subjectFromRefreshToken(String refreshToken) {
    Claims claims = parseRefreshTokenClaims(refreshToken);
    return claims.getSubject();
  }

  /**
   * 取得 Access Token 的 TTL（秒）。這個值可以用於前端定時刷新 Token，確保用戶體驗流暢，同時也方便前端根據實際需求調整刷新策略。
   * 
   * @return
   */
  public long getAccessTokenTtlSeconds() {
    return properties.getAccessTokenMinutes() * 60;
  }

  /**
   * 解析 Access Token。確保 Token 的類型正確，並且包含必要的 Claims（如 subject 和 roles）。如果 Token
   * 無效或過期，會拋出 JwtException，讓調用者能夠適當處理（如返回 401 Unauthorized）。
   * 
   * @param token
   * @return
   */
  private Claims parseAccessTokenClaims(String token) {
    Claims claims = Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token).getPayload();
    String typ = claims.get(JwtClaimNames.TOKEN_TYPE, String.class);
    if (!JwtClaimNames.TOKEN_TYPE_ACCESS.equals(typ)) {
      throw new JwtException("not an access token");
    }
    return claims;
  }

  /**
   * 解析 Refresh Token。確保 Token 的類型正確，並且包含必要的 Claims（如 subject）。如果 Token 無效或過期，會拋出
   * JwtException，讓調用者能夠適當處理（如返回 401 Unauthorized）。
   *
   * @param token
   * @return
   */
  private Claims parseRefreshTokenClaims(String token) {
    Claims claims = Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token).getPayload();
    String typ = claims.get(JwtClaimNames.TOKEN_TYPE, String.class);
    if (!JwtClaimNames.TOKEN_TYPE_REFRESH.equals(typ)) {
      throw new JwtException("not a refresh token");
    }
    return claims;
  }

  /**
   * 使用 SHA-256 雜湊處理 secret，確保符合 HS256 的安全建議（至少 32
   * bytes）。開發環境可使用簡單字串，生產環境請務必更換並保護好密鑰。
   * 
   * @return
   */
  private SecretKey signingKey() {
    try {
      MessageDigest digest = MessageDigest.getInstance(SIGNING_ALGORITHM);
      byte[] keyBytes = digest.digest(properties.getSecret().getBytes(StandardCharsets.UTF_8));
      return Keys.hmacShaKeyFor(keyBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 not available", e);
    }
  }
}
