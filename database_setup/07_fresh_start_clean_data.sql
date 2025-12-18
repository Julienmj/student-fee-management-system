-- ============================================
-- Fresh Start Script - Clean Data Only for Your Application
-- Only includes: SOFTWARE ENGINEERING, INFO MANAGEMENT, NETWORKING
-- ============================================

USE student_fees_db;

-- ============================================
-- CLEAR ALL EXISTING DATA
-- ============================================
-- Delete in correct order (child tables first)
DELETE FROM fees_payments;
DELETE FROM fees_enrollments;
DELETE FROM fees_students;
DELETE FROM fees_courses;
DELETE FROM fees_users;

-- Reset auto increment counters
ALTER TABLE fees_payments AUTO_INCREMENT = 1;
ALTER TABLE fees_enrollments AUTO_INCREMENT = 1;
ALTER TABLE fees_students AUTO_INCREMENT = 1;
ALTER TABLE fees_courses AUTO_INCREMENT = 1;
ALTER TABLE fees_users AUTO_INCREMENT = 1;

-- ============================================
-- Insert Staff Users
-- ============================================
INSERT INTO fees_users (username, password, role) VALUES
('registrar', 'reg123', 'REGISTRAR'),
('accountant', 'acc123', 'ACCOUNTANT'),
('admin', 'admin123', 'ADMIN');

-- ============================================
-- SOFTWARE ENGINEERING Courses
-- ============================================
INSERT INTO fees_courses (program, course_name, price_rwf, semester) VALUES
('SOFTWARE ENGINEERING', 'Programming Fundamentals', 150000.00, 1),
('SOFTWARE ENGINEERING', 'Object-Oriented Programming', 160000.00, 1),
('SOFTWARE ENGINEERING', 'Software Design Patterns', 170000.00, 1),
('SOFTWARE ENGINEERING', 'Web Application Development', 165000.00, 1),
('SOFTWARE ENGINEERING', 'Database Systems', 155000.00, 1),
('SOFTWARE ENGINEERING', 'Software Testing', 145000.00, 1);

-- ============================================
-- INFO MANAGEMENT Courses
-- ============================================
INSERT INTO fees_courses (program, course_name, price_rwf, semester) VALUES
('INFO MANAGEMENT', 'Information Systems Fundamentals', 140000.00, 1),
('INFO MANAGEMENT', 'Database Management', 150000.00, 1),
('INFO MANAGEMENT', 'Business Intelligence', 155000.00, 1),
('INFO MANAGEMENT', 'IT Project Management', 145000.00, 1),
('INFO MANAGEMENT', 'Systems Analysis and Design', 150000.00, 1),
('INFO MANAGEMENT', 'Enterprise Resource Planning', 160000.00, 1);

-- ============================================
-- NETWORKING Courses
-- ============================================
INSERT INTO fees_courses (program, course_name, price_rwf, semester) VALUES
('NETWORKING', 'Network Fundamentals', 145000.00, 1),
('NETWORKING', 'Routing and Switching', 160000.00, 1),
('NETWORKING', 'Network Security', 170000.00, 1),
('NETWORKING', 'Wireless Networks', 155000.00, 1),
('NETWORKING', 'Network Administration', 150000.00, 1),
('NETWORKING', 'Cloud Computing', 165000.00, 1);

-- ============================================
-- Sample Students (Only for the 3 programs)
-- ============================================
INSERT INTO fees_students (reg_number, full_name, program, password, total_fee) VALUES
('2025001', 'Iradukunda Smith', 'SOFTWARE ENGINEERING', 'pass123', 0.00),
('2025002', 'Niyonsenga Johnson', 'INFO MANAGEMENT', 'pass123', 0.00),
('2025003', 'Uwase Brown', 'NETWORKING', 'pass123', 0.00),
('2025004', 'Mugisha Garcia', 'SOFTWARE ENGINEERING', 'pass123', 0.00),
('2025005', 'Ishimwe Williams', 'INFO MANAGEMENT', 'pass123', 0.00);

-- ============================================
-- Enroll Students in Courses
-- ============================================

-- Student 1 (Iradukunda Smith) - SOFTWARE ENGINEERING courses
INSERT INTO fees_enrollments (student_id, course_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 5);

-- Student 2 (Niyonsenga Johnson) - INFO MANAGEMENT courses
INSERT INTO fees_enrollments (student_id, course_id) VALUES
(2, 7), (2, 8), (2, 9), (2, 10);

-- Student 3 (Uwase Brown) - NETWORKING courses
INSERT INTO fees_enrollments (student_id, course_id) VALUES
(3, 13), (3, 14), (3, 15), (3, 16);

-- Student 4 (Mugisha Garcia) - SOFTWARE ENGINEERING courses
INSERT INTO fees_enrollments (student_id, course_id) VALUES
(4, 1), (4, 2), (4, 4), (4, 6);

-- Student 5 (Ishimwe Williams) - INFO MANAGEMENT courses
INSERT INTO fees_enrollments (student_id, course_id) VALUES
(5, 7), (5, 8), (5, 11), (5, 12);

-- ============================================
-- Sample Payments
-- ============================================
INSERT INTO fees_payments (student_id, amount, method, note, paid_on) VALUES
(1, 150000.00, 'CASH', 'First installment', '2025-01-15'),
(1, 100000.00, 'MOMO', 'Second installment', '2025-02-10'),
(2, 140000.00, 'BANK', 'Partial payment', '2025-01-20'),
(3, 200000.00, 'CASH', 'Initial payment', '2025-01-18'),
(4, 150000.00, 'MOMO', 'First payment', '2025-01-25');

-- ============================================
-- VERIFICATION
-- ============================================
SELECT '=== DATABASE RESET COMPLETE ===' AS Status;

SELECT 'Courses by Program:' AS Info;
SELECT program, COUNT(*) AS course_count 
FROM fees_courses 
GROUP BY program 
ORDER BY program;

SELECT 'Students by Program:' AS Info;
SELECT program, COUNT(*) AS student_count 
FROM fees_students 
GROUP BY program 
ORDER BY program;

-- Show summary
SELECT 
    (SELECT COUNT(*) FROM fees_users) AS 'Staff Users',
    (SELECT COUNT(*) FROM fees_students) AS 'Students',
    (SELECT COUNT(*) FROM fees_courses) AS 'Courses',
    (SELECT COUNT(*) FROM fees_enrollments) AS 'Enrollments',
    (SELECT COUNT(*) FROM fees_payments) AS 'Payments';

SELECT 'Fresh start complete! Only SOFTWARE ENGINEERING, INFO MANAGEMENT, and NETWORKING data loaded.' AS Status;
