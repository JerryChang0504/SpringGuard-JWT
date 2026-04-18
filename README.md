# mvc-demo（Spring Boot）

單一模組 **Spring Boot 3** 專案（JDK 17），示範 **JWT** 簽發／校驗／refresh 與 **Spring Security** Filter 與端點權限。

## 專案結構

```text
src/main/java/com/gjun/lab/
  api/           啟動類別、REST Controller、示範帳號設定
  common/        共用 DTO、JWT Claim 常數
  security/      JwtTokenService、JwtAuthenticationFilter、SecurityConfiguration
src/main/resources/
  application.yml
  templates/     Thymeleaf：首頁、儀表板
  static/        `login-page.html`（登入 UI）、`css/app.css`
pom.xml
```

## 建置與執行

```bash
mvn spring-boot:run
```

或：

```bash
mvn clean package
java -jar target/mvc-demo-0.0.1-SNAPSHOT.jar
```

預設埠：`http://localhost:8080`

啟動後瀏覽：

- **`/`**：**首頁**（專案說明、測試帳號、前往登入／Swagger）
- **`/login`**：**登入頁**；登入成功進入 **`/welcome` 功能儀表板**（JWT 存於 `sessionStorage`，可一鍵示範公開／個人／管理員 API、刷新 Token、複製 access token）

本機有桌面環境時，預設會在啟動完成後嘗試自動開啟 **`http://127.0.0.1:8080/`**（首頁）。容器或無圖形介面會略過。若要關閉：`application.yml` 將 `app.browser.open-login` 設為 `false`。

若 IDE 以 **headless JVM** 執行導致無法開啟瀏覽器，可在執行參數加上 **`-Djava.awt.headless=false`**（僅本機示範用）。

**Swagger UI（springdoc-openAPI）**：`http://localhost:8080/swagger-ui.html`  
OpenAPI JSON：`/v3/api-docs`（僅列出 **`/api/**`** 端點）。請先在 UI 右上角 **Authorize** 貼上 `Bearer <accessToken>`（或直接貼 token，視 UI 版本而定）。

## 功能摘要

- `POST /api/auth/login`、`POST /api/auth/refresh`：取得 Token
- `GET /api/public/info`：公開
- `GET /api/user/profile`：需 `USER`
- `GET /api/admin/users`：需 `ADMIN`

示範帳號：`user` / `password`（USER）、`admin` / `admin`（USER + ADMIN）。

詳細說明請見程式註解與 `application.yml` 中的 `jwt.*` 設定。
