INSERT IGNORE INTO app_roles (id, name) VALUES (1, 'USER');
INSERT IGNORE INTO app_roles (id, name) VALUES (2, 'ADMIN');

-- 密碼皆為 BCrypt("password") 與 BCrypt("admin") 的範例雜湊（可用你的 PasswordEncoder 重新產生）
INSERT IGNORE INTO app_users (id, username, password_hash, enabled)
VALUES (1, 'user', '$2a$10$7EqJtq98hPqEX7fNZaFWoOa9G8d4c.1dK0WwVfJ7gYp8t0oZpTq6G', TRUE);

INSERT IGNORE INTO app_users (id, username, password_hash, enabled)
VALUES (2, 'admin', '$2a$10$h9S3kOa8m0oR3k4ZQxQz7O6c5o8tUeV5vXqkV5t7GgO7O9m2dY9m6', TRUE);

INSERT IGNORE INTO app_user_roles (user_id, role_id) VALUES (1, 1);
INSERT IGNORE INTO app_user_roles (user_id, role_id) VALUES (2, 1);
INSERT IGNORE INTO app_user_roles (user_id, role_id) VALUES (2, 2);

