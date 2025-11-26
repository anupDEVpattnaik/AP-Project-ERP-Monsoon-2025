USE auth_db;

INSERT INTO users_auth (username, role, password_hash, status)
VALUES ('admin', 'admin', '$2a$10$VcT4cxFnlF7YNFTDZa84pOQOd9xdXCfmY86tPmx5VnJN5uw22h5rC', 'active');