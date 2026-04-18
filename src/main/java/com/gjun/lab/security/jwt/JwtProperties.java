package com.gjun.lab.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * JWT 設定。
 */
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  // 32 bytes = 256 bits，符合 HS256 的安全建議。 開發環境使用簡單字串，生產環境請務必更換並保護好密鑰。
  private String secret = "dev-only-change-me-please-use-32bytes-min!!";
  // Access Token 的有效期較短，預設 15 分鐘。生產環境可根據需求調整，並確保妥善管理 Access Token 的存儲和撤銷機制。
  private long accessTokenMinutes = 15;
  // Refresh Token 的有效期較長，預設 7 天。生產環境可根據需求調整，並確保妥善管理 Refresh Token 的存儲和撤銷機制。
  private long refreshTokenDays = 7;
}
