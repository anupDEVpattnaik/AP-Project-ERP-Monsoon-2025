DROP DATABASE IF EXISTS auth_db;
CREATE DATABASE auth_db;
USE auth_db;

CREATE TABLE users_auth (
    user_id int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    username varchar(50) NOT NULL,
    role enum('student','instructor','admin') NOT NULL,
    password_hash varchar(255) NOT NULL,
    status enum('active','inactive','locked') DEFAULT 'active',
    last_login datetime DEFAULT NULL,
    failed_login_attempts int DEFAULT 0
);

CREATE TABLE password_history (
    log_id int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    user_id int NOT NULL,
    password_hash varchar(255) NOT NULL,
    changed_at datetime DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users_auth(user_id)
);