# Quick Start Guide - XAMPP MySQL Setup

## 🚀 Fast Setup (5 Minutes)

### Step 1: Start XAMPP (1 min)
1. Open **XAMPP Control Panel**
2. Click **Start** next to **MySQL**
3. Wait for green "Running" status

### Step 2: Create Database (2 min)
1. Open browser → Go to `http://localhost/phpmyadmin`
2. Click **SQL** tab
3. Copy & paste content from `01_create_database.sql` → Click **Go**
4. Copy & paste content from `02_create_tables.sql` → Click **Go**
5. Copy & paste content from `03_insert_sample_data.sql` → Click **Go**

### Step 3: Add MySQL Driver (1 min)
1. Download MySQL Connector/J from: https://dev.mysql.com/downloads/connector/j/
2. In NetBeans: Right-click project → **Properties** → **Libraries**
3. Click **Add JAR/Folder** → Select `mysql-connector-j-x.x.x.jar`
4. Click **OK**

### Step 4: Run Application (1 min)
1. In NetBeans: Press **Shift + F11** (Clean and Build)
2. Press **F6** (Run)
3. Login with test credentials below

---

## 🔑 Test Credentials

### Staff Login
| Role | Username | Password |
|------|----------|----------|
| Registrar | `registrar` | `reg123` |
| Accountant | `accountant` | `acc123` |

### Student Login
| Reg Number | Password | Program |
|------------|----------|---------|
| `2025001` | `pass123` | Computer Science |
| `2025002` | `pass123` | Business Administration |
| `2025003` | `pass123` | Engineering |
| `2025004` | `pass123` | Medicine |

---

## ✅ What Was Changed

### Files Created:
- ✅ `database_setup/01_create_database.sql` - Creates MySQL database
- ✅ `database_setup/02_create_tables.sql` - Creates all tables
- ✅ `database_setup/03_insert_sample_data.sql` - Inserts test data
- ✅ `database_setup/04_useful_queries.sql` - Helpful SQL queries
- ✅ `database_setup/SETUP_INSTRUCTIONS.md` - Detailed setup guide
- ✅ `database_setup/QUICK_START.md` - This file

### Files Modified:
- ✅ `src/database/DBConnection.java` - Updated for MySQL connection
- ✅ `src/database/StudentDAO.java` - Removed Oracle sequences
- ✅ `src/database/FeeAccountDAO.java` - Removed Oracle sequences
- ✅ `src/database/RegistrarDAO.java` - Removed Oracle sequences
- ✅ `src/database/StudentPortalDAO.java` - Removed Oracle sequences

---

## 🔧 Database Configuration

**Current Settings in DBConnection.java:**
```java
URL = "jdbc:mysql://localhost:3306/student_fees_db"
USERNAME = "root"
PASSWORD = ""  // Empty for default XAMPP
```

**If you set a MySQL password:**
Edit `src/database/DBConnection.java` line 15:
```java
private static final String PASSWORD = "your_password_here";
```

---

## 📊 Database Structure

### Tables:
1. **fees_users** - Staff accounts (Registrar, Accountant)
2. **fees_students** - Student records with login credentials
3. **fees_courses** - Course catalog with pricing
4. **fees_enrollments** - Links students to courses
5. **fees_payments** - Payment transaction history

### Sample Data:
- 3 Staff users
- 5 Students
- 20 Courses (4 programs)
- 17 Enrollments
- 5 Payment records

---

## ❗ Troubleshooting

### "Communications link failure"
→ MySQL not running. Start it in XAMPP Control Panel.

### "Access denied for user 'root'"
→ You set a MySQL password. Update `DBConnection.java` line 15.

### "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
→ MySQL Connector JAR not added. Follow Step 3 above.

### "Unknown database 'student_fees_db'"
→ Database not created. Run SQL scripts from Step 2.

---

## 📝 Next Steps

After successful setup:
1. Test all three login types (Registrar, Accountant, Student)
2. Explore the sample data
3. Try adding new students, courses, and payments
4. Review `04_useful_queries.sql` for helpful SQL queries
5. Read `SETUP_INSTRUCTIONS.md` for detailed documentation

---

## 🆘 Need Help?

Check the detailed guide: `SETUP_INSTRUCTIONS.md`

Common SQL queries: `04_useful_queries.sql`

---

**Your application is now ready to use with XAMPP MySQL!** 🎉
