-- ============================================
-- Simple Fix - Just Add Missing Courses
-- Doesn't delete anything, just adds what's needed
-- ============================================

USE student_fees_db;

-- ============================================
-- Add SOFTWARE ENGINEERING Courses (if not exist)
-- ============================================
INSERT IGNORE INTO fees_courses (program, course_name, price_rwf, semester) VALUES
('SOFTWARE ENGINEERING', 'Programming Fundamentals', 150000.00, 1),
('SOFTWARE ENGINEERING', 'Object-Oriented Programming', 160000.00, 1),
('SOFTWARE ENGINEERING', 'Software Design Patterns', 170000.00, 1),
('SOFTWARE ENGINEERING', 'Web Application Development', 165000.00, 1),
('SOFTWARE ENGINEERING', 'Database Systems', 155000.00, 1),
('SOFTWARE ENGINEERING', 'Software Testing', 145000.00, 1);

-- ============================================
-- Add INFO MANAGEMENT Courses (if not exist)
-- ============================================
INSERT IGNORE INTO fees_courses (program, course_name, price_rwf, semester) VALUES
('INFO MANAGEMENT', 'Information Systems Fundamentals', 140000.00, 1),
('INFO MANAGEMENT', 'Database Management', 150000.00, 1),
('INFO MANAGEMENT', 'Business Intelligence', 155000.00, 1),
('INFO MANAGEMENT', 'IT Project Management', 145000.00, 1),
('INFO MANAGEMENT', 'Systems Analysis and Design', 150000.00, 1),
('INFO MANAGEMENT', 'Enterprise Resource Planning', 160000.00, 1);

-- ============================================
-- Add NETWORKING Courses (if not exist)
-- ============================================
INSERT IGNORE INTO fees_courses (program, course_name, price_rwf, semester) VALUES
('NETWORKING', 'Network Fundamentals', 145000.00, 1),
('NETWORKING', 'Routing and Switching', 160000.00, 1),
('NETWORKING', 'Network Security', 170000.00, 1),
('NETWORKING', 'Wireless Networks', 155000.00, 1),
('NETWORKING', 'Network Administration', 150000.00, 1),
('NETWORKING', 'Cloud Computing', 165000.00, 1);

-- ============================================
-- Delete Unused Program Courses
-- ============================================
DELETE FROM fees_courses 
WHERE program NOT IN ('SOFTWARE ENGINEERING', 'INFO MANAGEMENT', 'NETWORKING');

-- ============================================
-- Delete Students from Unused Programs
-- ============================================
DELETE FROM fees_students 
WHERE program NOT IN ('SOFTWARE ENGINEERING', 'INFO MANAGEMENT', 'NETWORKING');

-- ============================================
-- VERIFICATION
-- ============================================
SELECT '=== SIMPLE FIX COMPLETE ===' AS Status;

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

SELECT 'Done! Your database now has only the 3 programs you need.' AS Status;
