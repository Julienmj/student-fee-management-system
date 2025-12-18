-- ============================================
-- Cleanup Script - Remove Unused Program Data
-- Removes courses and students for programs NOT used in the application
-- ============================================

USE student_fees_db;

-- ============================================
-- BACKUP CHECK - Show what will be deleted
-- ============================================
SELECT '=== COURSES TO BE DELETED ===' AS Info;
SELECT course_id, program, course_name, price_rwf 
FROM fees_courses 
WHERE program NOT IN ('SOFTWARE ENGINEERING', 'INFO MANAGEMENT', 'NETWORKING')
ORDER BY program, course_name;

SELECT '=== STUDENTS TO BE DELETED ===' AS Info;
SELECT student_id, reg_number, full_name, program 
FROM fees_students 
WHERE program NOT IN ('SOFTWARE ENGINEERING', 'INFO MANAGEMENT', 'NETWORKING')
ORDER BY program, full_name;

-- ============================================
-- DELETE UNUSED DATA
-- Note: Foreign keys will cascade delete related enrollments and payments
-- ============================================

-- Delete students from unused programs (cascades to enrollments and payments)
DELETE FROM fees_students 
WHERE program NOT IN ('SOFTWARE ENGINEERING', 'INFO MANAGEMENT', 'NETWORKING');

-- Delete courses from unused programs (cascades to enrollments)
DELETE FROM fees_courses 
WHERE program NOT IN ('SOFTWARE ENGINEERING', 'INFO MANAGEMENT', 'NETWORKING');

-- ============================================
-- VERIFICATION - Show remaining data
-- ============================================
SELECT '=== CLEANUP COMPLETE ===' AS Status;

SELECT 'Remaining Courses by Program:' AS Info;
SELECT program, COUNT(*) AS course_count 
FROM fees_courses 
GROUP BY program 
ORDER BY program;

SELECT 'Remaining Students by Program:' AS Info;
SELECT program, COUNT(*) AS student_count 
FROM fees_students 
GROUP BY program 
ORDER BY program;

-- Show total counts
SELECT 
    (SELECT COUNT(*) FROM fees_courses) AS total_courses,
    (SELECT COUNT(*) FROM fees_students) AS total_students,
    (SELECT COUNT(*) FROM fees_enrollments) AS total_enrollments,
    (SELECT COUNT(*) FROM fees_payments) AS total_payments;

SELECT 'Database cleaned successfully!' AS Status;
