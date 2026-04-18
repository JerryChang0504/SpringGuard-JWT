當然！以下是 `JwtTokenService` 的 Markdown 格式原始源代碼：

````markdown
# JwtTokenService

`JwtTokenService` 是 `com.gjun.lab.security.jwt` 包中的服務類別。它負責生成和管理用於應用程序身份驗證的 JSON Web Tokens（JWT）。

## 依賴項

`JwtTokenService` 有以下依賴項：

- `JwtProperties`：這是一個配置類別，保存與JWT相關的屬性，例如密鑰、存取令牌過期時間和刷新令牌過期時間。

## 建構子

`JwtTokenService` 的建構子接受一個 `JwtProperties` 實例作為參數：

```java
public JwtTokenService(JwtProperties properties) {
    this.properties = properties;
}
```
````

## 方法

### `createAccessToken(UserDetails user)`

這個方法為給定的 `UserDetails` 物件生成一個存取令牌。它執行以下步驟：

1. 驗證 `user` 物件不為空。
2. 取得目前時間。
3. 取得用戶的權限（角色）。
4. 使用用戶的名稱和權限構建JWT的載荷。
5. 使用密鑰簽名令牌並設置過期時間。
6. 返回JWT的緊縮表示形式。

### `createRefreshToken(String username)`

這個方法為給定的 `username` 生成一個刷新令牌。它執行以下步驟：

1. 驗證 `username` 不為空。
2. 使用 `username` 和令牌類型構建JWT的載荷。
3. 使用密鑰簽名令牌並設置過期時間。
4. 返回JWT的緊縮表示形式。

### `getAuthenticationFromAccessToken(String token)`

這個方法接受一個JWT令牌作為輸入，並返回一個 `Authentication` 物件。它執行以下步驟：

1. 解析JWT的載荷。
2. 從載荷中取出用戶名。
3. 使用用戶名從 `UserDetailsService` 加載用戶詳細資訊。
4. 使用用戶詳細資訊和權限構建一個 `UsernamePasswordAuthenticationToken` 物件。
5. 返回驗證物件。

### `subjectFromRefreshToken(String refreshToken)`

這個方法接受一個刷新令牌作為輸入，並返回與令牌相關的用戶名。它執行以下步驟：

1. 解析從刷新令牌中提取的JWT載荷。
2. 從載荷中取出用戶名。
3. 返回用戶名。

### `getAccessTokenTtlSeconds()`

這個方法返回存取令牌的過期時間（以秒為單位）。

### `signingKey()`

這個方法返回用於簽名JWT的密鑰。

### `parseAccessTokenClaims(String token)`

這個方法解析存取令牌的載荷。

### `parseRefreshTokenClaims(String refreshToken)`

這個方法解析刷新令牌的載荷。

## 使用方法

要使用 `JwtTokenService`，您可以將它注入控制器或服務類別中：

```java
@Autowired
private JwtTokenService jwtTokenService;
```

然後，您可以調用 `JwtTokenService` 的方法來生成存取令牌、刷新令牌和從令牌中提取用戶信息。

## 配置

要配置 `JwtTokenService`，您需要在 `JwtProperties` 類別中提供必要的屬性。您可以在應用程序的配置文件中（例如 `application.properties` 或 `application.yml`）中定義這些屬性。

## 依賴項

要使用 `JwtTokenService`，您需要在項目中包含以下依賴項：

- `io.jsonwebtoken:jjwt-api`
- `io.jsonwebtoken:jjwt-impl`

```

請注意，此文件假設存在必要的依賴項和導入。
```
