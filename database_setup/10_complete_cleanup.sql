-- ============================================
-- Complete Cleanup - Remove Duplicates & Unused Data
-- Safe to run multiple times
-- ============================================

USE student_fees_db;

-- ============================================
-- STEP 1: Remove ALL duplicate courses
-- ============================================
SELECT '=== STEP 1: Removing Duplicates ===' AS Status;

-- Delete duplicates (keeps the first occurrence)
DELETE c1 FROM fees_courses c1
INNER JOIN fees_courses c2 
WHERE c1.course_id > c2.course_id 
  AND c1.program = c2.program 
  AND c1.course_name = c2.course_name;

-- ============================================
-- STEP 2: Delete courses from unused programs
-- ============================================
SELECT '=== STEP 2: Removing Unused Programs ===' AS Status;

DELETE FROM fees_courses 
WHERE program NOT IN ('SOFTWARE ENGINEERING', 'INFO MANAGEMENT', 'NETWORKING');

-- ============================================
-- STEP 3: Delete students from unused programs
-- ============================================
DELETE FROM fees_students 
WHERE program NOT IN ('SOFTWARE ENGINEERING', 'INFO MANAGEMENT', 'NETWORKING');

-- ============================================
-- STEP 4: Add missing courses (if needed)
-- ============================================
SELECT '=== STEP 3: Adding Missing Courses ===' AS Status;

-- SOFTWARE ENGINEERING
INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'SOFTWARE ENGINEERING', 'Programming Fundamentals', 150000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'SOFTWARE ENGINEERING' 
    AND course_name = 'Programming Fundamentals'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'SOFTWARE ENGINEERING', 'Object-Oriented Programming', 160000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'SOFTWARE ENGINEERING' 
    AND course_name = 'Object-Oriented Programming'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'SOFTWARE ENGINEERING', 'Software Design Patterns', 170000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'SOFTWARE ENGINEERING' 
    AND course_name = 'Software Design Patterns'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'SOFTWARE ENGINEERING', 'Web Application Development', 165000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'SOFTWARE ENGINEERING' 
    AND course_name = 'Web Application Development'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'SOFTWARE ENGINEERING', 'Database Systems', 155000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'SOFTWARE ENGINEERING' 
    AND course_name = 'Database Systems'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'SOFTWARE ENGINEERING', 'Software Testing', 145000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'SOFTWARE ENGINEERING' 
    AND course_name = 'Software Testing'
);

-- INFO MANAGEMENT
INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'INFO MANAGEMENT', 'Information Systems Fundamentals', 140000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'INFO MANAGEMENT' 
    AND course_name = 'Information Systems Fundamentals'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'INFO MANAGEMENT', 'Database Management', 150000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'INFO MANAGEMENT' 
    AND course_name = 'Database Management'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'INFO MANAGEMENT', 'Business Intelligence', 155000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'INFO MANAGEMENT' 
    AND course_name = 'Business Intelligence'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'INFO MANAGEMENT', 'IT Project Management', 145000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'INFO MANAGEMENT' 
    AND course_name = 'IT Project Management'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'INFO MANAGEMENT', 'Systems Analysis and Design', 150000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'INFO MANAGEMENT' 
    AND course_name = 'Systems Analysis and Design'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'INFO MANAGEMENT', 'Enterprise Resource Planning', 160000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'INFO MANAGEMENT' 
    AND course_name = 'Enterprise Resource Planning'
);

-- NETWORKING
INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'NETWORKING', 'Network Fundamentals', 145000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'NETWORKING' 
    AND course_name = 'Network Fundamentals'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'NETWORKING', 'Routing and Switching', 160000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'NETWORKING' 
    AND course_name = 'Routing and Switching'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'NETWORKING', 'Network Security', 170000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'NETWORKING' 
    AND course_name = 'Network Security'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'NETWORKING', 'Wireless Networks', 155000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'NETWORKING' 
    AND course_name = 'Wireless Networks'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'NETWORKING', 'Network Administration', 150000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'NETWORKING' 
    AND course_name = 'Network Administration'
);

INSERT INTO fees_courses (program, course_name, price_rwf, semester)
SELECT 'NETWORKING', 'Cloud Computing', 165000.00, 1
WHERE NOT EXISTS (
    SELECT 1 FROM fees_courses 
    WHERE program = 'NETWORKING' 
    AND course_name = 'Cloud Computing'
);

-- ============================================
-- VERIFICATION
-- ============================================
SELECT '=== CLEANUP COMPLETE ===' AS Status;

SELECT 'Courses by Program:' AS Info;
SELECT program, COUNT(*) AS course_count 
FROM fees_courses 
GROUP BY program 
ORDER BY program;

SELECT 'All Courses (No Duplicates):' AS Info;
SELECT course_id, program, course_name, price_rwf
FROM fees_courses
ORDER BY program, course_name;

-- Check for any remaining duplicates (should return empty)
SELECT 'Duplicate Check (Should be empty):' AS Info;
SELECT program, course_name, COUNT(*) AS count
FROM fees_courses
GROUP BY program, course_name
HAVING COUNT(*) > 1;

SELECT 'Success! Database is clean with no duplicates.' AS Status;
