-- ============================================
-- Cleanup Auto-Generated Sample Students
-- Removes students created by "Generate 10 Sample Students" button
-- ============================================

USE student_fees_db;

-- Show students that will be deleted
SELECT '=== STUDENTS TO BE REMOVED ===' AS Info;
SELECT student_id, reg_number, full_name, program 
FROM fees_students
WHERE reg_number >= '2025006'
ORDER BY reg_number;

-- Delete auto-generated students (2025006 and above)
DELETE FROM fees_students 
WHERE reg_number >= '2025006';

-- Keep only the original 5 sample students (2025001-2025005)
SELECT '=== REMAINING STUDENTS ===' AS Info;
SELECT student_id, reg_number, full_name, program 
FROM fees_students
ORDER BY reg_number;

SELECT 'Cleanup complete! Only manually registered students remain.' AS Status;
