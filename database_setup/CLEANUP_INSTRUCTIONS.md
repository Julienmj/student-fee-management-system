# Database Cleanup Instructions

## 🎯 Purpose
Remove all courses and students for programs NOT used in your application.

**Your Application Uses:**
- SOFTWARE ENGINEERING
- INFO MANAGEMENT
- NETWORKING

**Will Remove:**
- Computer Science
- Business Administration
- Engineering
- Medicine

---

## 📋 Choose Your Cleanup Method

### **Option 1: Keep Existing Data + Remove Unused** (Recommended if you already added students)
Use this if you want to keep any students you've already registered.

**Script**: `06_cleanup_unused_data.sql`

**What it does:**
- ✅ Keeps all SOFTWARE ENGINEERING data
- ✅ Keeps all INFO MANAGEMENT data
- ✅ Keeps all NETWORKING data
- ❌ Deletes Computer Science courses/students
- ❌ Deletes Business Administration courses/students
- ❌ Deletes Engineering courses/students
- ❌ Deletes Medicine courses/students

---

### **Option 2: Fresh Start with Clean Sample Data** (Recommended for clean slate)
Use this for a completely fresh start with only relevant sample data.

**Script**: `07_fresh_start_clean_data.sql`

**What it does:**
- 🔄 Clears ALL existing data
- ✅ Adds 3 staff users
- ✅ Adds 18 courses (6 per program)
- ✅ Adds 5 sample students (only for the 3 programs)
- ✅ Adds sample enrollments
- ✅ Adds sample payments

---

## 🚀 How to Run

### Step 1: Choose Your Script
- **Option 1**: `06_cleanup_unused_data.sql` (keep existing + cleanup)
- **Option 2**: `07_fresh_start_clean_data.sql` (fresh start)

### Step 2: Run in phpMyAdmin
1. Open browser → `http://localhost/phpmyadmin`
2. Click `student_fees_db` database
3. Click **SQL** tab
4. Open your chosen script file
5. Copy ALL content
6. Paste into SQL box
7. Click **Go**

### Step 3: Verify Results
You should see:
```
Courses by Program:
- INFO MANAGEMENT: 6 courses
- NETWORKING: 6 courses
- SOFTWARE ENGINEERING: 6 courses

Students by Program:
- INFO MANAGEMENT: varies
- NETWORKING: varies
- SOFTWARE ENGINEERING: varies

Total: 18 courses (only the 3 programs)
```

### Step 4: Rebuild & Test
1. NetBeans: **Shift + F11** (Clean and Build)
2. **F6** (Run)
3. Login and test

---

## 📊 Before vs After

### Before Cleanup:
```
Programs: 7 (Computer Science, Business Admin, Engineering, Medicine, 
             SOFTWARE ENGINEERING, INFO MANAGEMENT, NETWORKING)
Courses: 38
Students: Mixed programs
```

### After Cleanup:
```
Programs: 3 (SOFTWARE ENGINEERING, INFO MANAGEMENT, NETWORKING)
Courses: 18 (6 per program)
Students: Only for the 3 programs
```

---

## ⚠️ Important Notes

1. **Backup First** (Optional but recommended):
   - In phpMyAdmin: Select database → Export → Go
   - Saves a backup SQL file

2. **Foreign Keys**: 
   - Deleting courses/students automatically deletes related enrollments and payments
   - This is by design (CASCADE DELETE)

3. **No Undo**:
   - Once deleted, data cannot be recovered
   - Make sure you choose the right option

4. **Sample Data**:
   - Option 2 includes fresh sample data for testing
   - Option 1 keeps whatever data you have

---

## 🧪 Test After Cleanup

### Test Checklist:
- [ ] Login as Registrar works
- [ ] All 3 programs show in dropdown
- [ ] Each program shows 6 courses
- [ ] Can register new student
- [ ] Enrolled list displays correctly
- [ ] Accountant dashboard shows data
- [ ] Student login works

---

## 🔄 If You Need to Revert

If something goes wrong, you can restore the original data:

1. Run `01_create_database.sql` (drops and recreates database)
2. Run `02_create_tables.sql` (creates tables)
3. Run `07_fresh_start_clean_data.sql` (loads clean data)

---

## 💡 Recommendation

**For most users**: Use **Option 2** (`07_fresh_start_clean_data.sql`)

**Why?**
- ✅ Clean slate with only relevant data
- ✅ Consistent sample data for testing
- ✅ No confusion with old programs
- ✅ Faster and cleaner

**When to use Option 1?**
- You've already registered many students
- You want to keep existing payment records
- You just want to remove the unused programs

---

## 📝 Summary

| Script | Use Case | Data Loss |
|--------|----------|-----------|
| `06_cleanup_unused_data.sql` | Keep existing + cleanup | Only unused programs |
| `07_fresh_start_clean_data.sql` | Fresh start | All existing data |

Choose based on your needs and run the appropriate script!

---

**Ready?** Pick your script and run it in phpMyAdmin! 🚀
