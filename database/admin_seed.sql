USE auth_db;

INSERT INTO users_auth (username, role, password_hash, status)
VALUES ('admin', 'admin', '$2a$10$VcT4cxFnlF7YNFTDZa84pOQOd9xdXCfmY86tPmx5VnJN5uw22h5rC', 'active');

INSERT INTO users_auth (username, role, password_hash, status, last_login)
VALUES
    ('inst1',  'instructor','$2a$10$4JfxD.gDYRzdgcovdi2vqunPFLC5D9M6NVEg0pNUPeaeRsOWizRNC', 'active', NULL),
    ('stu1',   'student',  '$2a$10$M5DEs8j8psBTqmvVfgCxw.suwn2LO0oQi0.mjrybOyUz7S8dNn/Au', 'active', NULL),
    ('stu2',   'student',  '$2a$10$M5DEs8j8psBTqmvVfgCxw.suwn2LO0oQi0.mjrybOyUz7S8dNn/Au', 'active', NULL);
