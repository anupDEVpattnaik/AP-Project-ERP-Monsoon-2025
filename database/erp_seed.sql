USE erp_db;

DELETE FROM grades;
DELETE FROM enrollments;
DELETE FROM sections;
DELETE FROM courses;
DELETE FROM instructors;
DELETE FROM students;
DELETE FROM settings;

INSERT INTO students (user_id, roll_no, program, year)
VALUES
    (3, '2025001', 'B.Tech', 1),
    (4, '2025002', 'B.Tech', 2);

INSERT INTO instructors (user_id, department)
VALUES
    (2, 'CSE');

INSERT INTO courses (code, title, credits)
VALUES
    ('CSE101', 'Introduction to Programming', 4);

INSERT INTO sections
(section_id, course_id, instructor_id, day_time, room, capacity, semester, year)
VALUES
    (1, 'CSE101', 2, 'Mon 10AM', 'C101', 50, 'Monsoon', 2025);

INSERT INTO enrollments (enrollment_id, student_id, section_id, status)
VALUES
    (1, 3, 1, 'enrolled');

INSERT INTO grades(grade_id, enrollment_id, component, score, final_grade)
VALUES
    (1, 1, 'quiz', NULL, NULL),
    (2, 1, 'midterm', NULL, NULL),
    (3, 1, 'endsem', NULL, NULL);

INSERT INTO settings (setting_key, setting_value)
VALUES ('maintenance_mode', 'off');
