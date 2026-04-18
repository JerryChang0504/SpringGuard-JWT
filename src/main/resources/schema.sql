CREATE TABLE IF NOT EXISTS app_users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(128) NOT NULL UNIQUE,
  password_hash VARCHAR(200) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS app_roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS app_user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_app_user_roles_user FOREIGN KEY (user_id) REFERENCES app_users(id),
  CONSTRAINT fk_app_user_roles_role FOREIGN KEY (role_id) REFERENCES app_roles(id)
);

