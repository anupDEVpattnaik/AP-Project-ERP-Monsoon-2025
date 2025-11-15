DROP DATABASE IF EXISTS erp_db;
CREATE DATABASE erp_db;
USE erp_db;

CREATE TABLE courses (
    code char(6) PRIMARY KEY,
    title varchar(100) NOT NULL,
    credits int 
);

CREATE TABLE students (
    user_id int PRIMARY KEY,
    roll_no varchar(7) NOT NULL,
    program enum('B.Tech','M.Tech') NOT NULL,
    year int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES auth_db.users_auth(user_id)
);

CREATE TABLE instructors (
    user_id int PRIMARY KEY,
    department varchar(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES auth_db.users_auth(user_id)
);

CREATE TABLE sections (
    section_id int AUTO_INCREMENT PRIMARY KEY,
    course_id varchar(6) NOT NULL,
    instructor_id int NOT NULL,
    day_time varchar(50),
    room varchar(20),
    capacity int,
    semester enum('Monsoon','Winter','Summer') NOT NULL,
    year year NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(code),
    FOREIGN KEY (instructor_id) REFERENCES instructors(user_id)
);

CREATE TABLE enrollments (
    enrollment_id int AUTO_INCREMENT PRIMARY KEY,
    student_id int NOT NULL,
    section_id int NOT NULL,
    status enum('enrolled','dropped') DEFAULT 'enrolled',
    UNIQUE(student_id,section_id),
    FOREIGN KEY (student_id) REFERENCES students(user_id),
    FOREIGN KEY (section_id) REFERENCES sections(section_id)
);

CREATE TABLE grades (
    grade_id int AUTO_INCREMENT PRIMARY KEY,
    enrollment_id int NOT NULL,
    component varchar(30) NOT NULL,
    score decimal(5,2) CHECK ((score >= 0)) ,
    final_grade char(2),
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id)
);

CREATE TABLE settings (
    setting_key varchar(50) PRIMARY KEY,
    setting_value varchar(50) NOT NULL
);