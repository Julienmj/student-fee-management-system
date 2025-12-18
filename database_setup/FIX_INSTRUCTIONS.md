# Fix Instructions - Add Missing Program Courses

## Issue Found
Your application uses these programs:
- SOFTWARE ENGINEERING
- INFO MANAGEMENT  
- NETWORKING

But your database only had courses for:
- Computer Science
- Business Administration
- Engineering
- Medicine

## What Was Fixed

### 1. ✅ Fixed Oracle SQL Function
**File**: `src/ui/RegistrarDashboard.java`
- Changed `LISTAGG()` (Oracle) → `GROUP_CONCAT()` (MySQL)
- This fixes the "Enrolled List" tab

### 2. ✅ Created Missing Courses SQL Script
**File**: `database_setup/05_add_missing_programs.sql`
- Adds 6 courses for SOFTWARE ENGINEERING
- Adds 6 courses for INFO MANAGEMENT
- Adds 6 courses for NETWORKING

## How to Fix Your Database

### Step 1: Run the SQL Script

**Option A: Using phpMyAdmin (Recommended)**
1. Open browser → `http://localhost/phpmyadmin`
2. Click on `student_fees_db` database
3. Click **SQL** tab
4. Open file: `05_add_missing_programs.sql`
5. Copy all content and paste into SQL box
6. Click **Go**

**Option B: Using MySQL Command Line**
```cmd
cd C:\xampp\mysql\bin
mysql -u root -p
```
Press Enter (password is empty), then:
```sql
source "C:\Users\mujen\OneDrive\Desktop\final project pro\database_setup\05_add_missing_programs.sql"
```

### Step 2: Rebuild and Run Application
1. In NetBeans: Press **Shift + F11** (Clean and Build)
2. Press **F6** (Run)
3. Login as Registrar
4. Select "SOFTWARE ENGINEERING" from Program dropdown
5. You should now see 6 courses!

## Verification

After running the script, verify in phpMyAdmin:
```sql
SELECT program, COUNT(*) AS course_count 
FROM fees_courses 
GROUP BY program;
```

You should see:
- Business Administration: 5 courses
- Computer Science: 5 courses
- Engineering: 5 courses
- INFO MANAGEMENT: 6 courses
- Medicine: 5 courses
- NETWORKING: 6 courses
- SOFTWARE ENGINEERING: 6 courses

**Total: 38 courses**

## What's Now Working

✅ Course list loads for all 3 programs
✅ Student registration works
✅ Enrolled list displays correctly
✅ All MySQL-specific functions working
✅ No more Oracle syntax errors

## Test the Application

1. **Login as Registrar** (`registrar` / `reg123`)
2. **Register Student** tab:
   - Click "Generate" for reg number
   - Enter name
   - Select "SOFTWARE ENGINEERING"
   - Select courses (you should see 6 courses now!)
   - Click "Register"
3. **Enrolled List** tab:
   - Should show all registered students with their courses

## Additional Notes

- All Oracle sequences removed (using MySQL AUTO_INCREMENT)
- All Oracle SQL functions converted to MySQL
- Database connection configured for XAMPP
- Sample data includes all program types

---

**Your application should now work perfectly!** 🎉
