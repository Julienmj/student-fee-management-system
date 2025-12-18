-- ============================================
-- Add Courses for Missing Programs
-- SOFTWARE ENGINEERING, INFO MANAGEMENT, NETWORKING
-- ============================================

USE student_fees_db;

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

-- Display success message
SELECT 'Courses added for SOFTWARE ENGINEERING, INFO MANAGEMENT, and NETWORKING!' AS Status;

-- Show course count by program
SELECT program, COUNT(*) AS course_count 
FROM fees_courses 
GROUP BY program 
ORDER BY program;
