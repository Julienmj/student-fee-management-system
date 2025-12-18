# Database Setup Files

This folder contains all necessary files to set up your Student Fee Management System with XAMPP MySQL.

## 📁 Files Overview

| File | Purpose | Order |
|------|---------|-------|
| `QUICK_START.md` | **START HERE** - Fast 5-minute setup guide | Read First |
| `01_create_database.sql` | Creates the MySQL database | Run 1st |
| `02_create_tables.sql` | Creates all tables with proper structure | Run 2nd |
| `03_insert_sample_data.sql` | Inserts test data for immediate use | Run 3rd |
| `04_useful_queries.sql` | Collection of helpful SQL queries | Reference |
| `05_add_missing_programs.sql` | Adds courses for SOFTWARE ENGINEERING, INFO MANAGEMENT, NETWORKING | Optional |
| `06_cleanup_unused_data.sql` | Removes unused program data (keeps existing) | Cleanup |
| `07_fresh_start_clean_data.sql` | Fresh start with only 3 programs | Cleanup |
| `SETUP_INSTRUCTIONS.md` | Detailed setup documentation | Reference |
| `CLEANUP_INSTRUCTIONS.md` | Database cleanup guide | Reference |
| `README.md` | This file | Info |

## 🚀 Quick Setup

1. **Read**: `QUICK_START.md` (5 minutes)
2. **Execute**: SQL files in order (01 → 02 → 03)
3. **Run**: Your Java application

## 📋 What Gets Created

### Database: `student_fees_db`

### Tables (5):
- `fees_users` - Staff accounts
- `fees_students` - Student records  
- `fees_courses` - Course catalog
- `fees_enrollments` - Student-course links
- `fees_payments` - Payment transactions

### Sample Data:
- 3 staff users (registrar, accountant, admin)
- 5 students across 4 programs
- 20 courses with pricing
- 17 course enrollments
- 5 payment records

## 🔑 Default Credentials

**Registrar**: `registrar` / `reg123`  
**Accountant**: `accountant` / `acc123`  
**Student**: `2025001` / `pass123`

## ⚙️ Configuration

**Database**: `student_fees_db`  
**Host**: `localhost:3306`  
**User**: `root`  
**Password**: (empty by default)

To change password: Edit `src/database/DBConnection.java` line 15

## 📖 Documentation

- **Quick Setup**: See `QUICK_START.md`
- **Detailed Guide**: See `SETUP_INSTRUCTIONS.md`
- **SQL Reference**: See `04_useful_queries.sql`

## ✅ Migration from Oracle

All Oracle-specific code has been converted to MySQL:
- ✅ Sequences → AUTO_INCREMENT
- ✅ Oracle syntax → MySQL syntax
- ✅ Connection string updated
- ✅ JDBC driver changed

## 🆘 Support

If you encounter issues:
1. Check `SETUP_INSTRUCTIONS.md` troubleshooting section
2. Verify XAMPP MySQL is running
3. Ensure MySQL Connector/J JAR is added to project
4. Check Java console for error messages

---

**Ready to start?** Open `QUICK_START.md` and follow the 5-minute setup! 🎉
