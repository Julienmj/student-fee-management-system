# Student Fee Management System - XAMPP MySQL Setup Guide

## Prerequisites
- XAMPP installed on your Windows PC
- Java Development Kit (JDK) installed
- NetBeans IDE (or any Java IDE)

---

## Step 1: Install and Start XAMPP

1. **Download XAMPP** (if not already installed):
   - Visit: https://www.apachefriends.org/
   - Download the Windows version
   - Install XAMPP to default location (C:\xampp)

2. **Start XAMPP Services**:
   - Open XAMPP Control Panel
   - Click **Start** for **Apache** (optional, for web interface)
   - Click **Start** for **MySQL** (required)
   - Ensure MySQL shows "Running" status

---

## Step 2: Create Database Using phpMyAdmin

### Option A: Using phpMyAdmin Web Interface (Recommended)

1. **Open phpMyAdmin**:
   - Open your web browser
   - Go to: `http://localhost/phpmyadmin`
   - Default login: username = `root`, password = (leave empty)

2. **Execute SQL Scripts**:
   - Click on the **SQL** tab at the top
   - Open `01_create_database.sql` in a text editor
   - Copy all content and paste into the SQL query box
   - Click **Go** to execute
   
3. **Create Tables**:
   - The database `student_fees_db` should now appear in the left sidebar
   - Click on `student_fees_db` to select it
   - Click on the **SQL** tab again
   - Open `02_create_tables.sql` in a text editor
   - Copy all content and paste into the SQL query box
   - Click **Go** to execute

4. **Insert Sample Data**:
   - With `student_fees_db` still selected
   - Click on the **SQL** tab
   - Open `03_insert_sample_data.sql` in a text editor
   - Copy all content and paste into the SQL query box
   - Click **Go** to execute

### Option B: Using MySQL Command Line

1. **Open Command Prompt**:
   - Press `Win + R`
   - Type `cmd` and press Enter

2. **Navigate to MySQL bin directory**:
   ```cmd
   cd C:\xampp\mysql\bin
   ```

3. **Login to MySQL**:
   ```cmd
   mysql -u root -p
   ```
   - Press Enter when prompted for password (default is empty)

4. **Execute SQL Scripts**:
   ```sql
   source "C:\Users\mujen\OneDrive\Desktop\final project pro\database_setup\01_create_database.sql"
   source "C:\Users\mujen\OneDrive\Desktop\final project pro\database_setup\02_create_tables.sql"
   source "C:\Users\mujen\OneDrive\Desktop\final project pro\database_setup\03_insert_sample_data.sql"
   ```

5. **Verify Installation**:
   ```sql
   USE student_fees_db;
   SHOW TABLES;
   SELECT * FROM fees_users;
   ```

---

## Step 3: Add MySQL JDBC Driver to Your Project

1. **Download MySQL Connector/J**:
   - Visit: https://dev.mysql.com/downloads/connector/j/
   - Download the latest version (Platform Independent ZIP)
   - Extract the ZIP file

2. **Add to NetBeans Project**:
   - In NetBeans, right-click on your project
   - Select **Properties**
   - Go to **Libraries** category
   - Click **Add JAR/Folder**
   - Navigate to the extracted MySQL Connector folder
   - Select `mysql-connector-j-x.x.x.jar` (or `mysql-connector-java-x.x.x.jar`)
   - Click **Open**, then **OK**

3. **Alternative: Copy to lib folder**:
   - Copy the JAR file to your project's `lib` folder
   - Right-click project → Properties → Libraries → Add JAR/Folder
   - Select the JAR from the lib folder

---

## Step 4: Verify Database Connection

1. **Check DBConnection.java**:
   - File location: `src/database/DBConnection.java`
   - Verify these settings:
     ```java
     URL = "jdbc:mysql://localhost:3306/student_fees_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
     USERNAME = "root"
     PASSWORD = ""  // Empty for default XAMPP
     ```

2. **If you set a MySQL password**:
   - Update the PASSWORD field in DBConnection.java
   - Example: `private static final String PASSWORD = "your_password";`

---

## Step 5: Run Your Application

1. **Clean and Build**:
   - In NetBeans: **Run** → **Clean and Build Project**
   - Or press `Shift + F11`

2. **Run the Application**:
   - Click **Run** → **Run Project**
   - Or press `F6`

3. **Test Login**:
   - Use these default credentials:

   **Registrar Login:**
   - Username: `registrar`
   - Password: `reg123`

   **Accountant Login:**
   - Username: `accountant`
   - Password: `acc123`

   **Student Login:**
   - Registration Number: `2025001` (or 2025002, 2025003, etc.)
   - Password: `pass123`

---

## Database Schema Overview

### Tables Created:

1. **fees_users** - Staff users (Registrar, Accountant)
2. **fees_students** - Student records
3. **fees_courses** - Course catalog with pricing
4. **fees_enrollments** - Student course enrollments
5. **fees_payments** - Payment transactions

### Sample Data Included:

- **3 Staff Users** (registrar, accountant, admin)
- **5 Students** across different programs
- **20 Courses** (Computer Science, Business, Engineering, Medicine)
- **Sample Enrollments** and **Payments**

---

## Troubleshooting

### Issue: "Communications link failure"
**Solution**: 
- Ensure MySQL is running in XAMPP Control Panel
- Check if port 3306 is not blocked by firewall
- Verify database name is correct: `student_fees_db`

### Issue: "Access denied for user 'root'@'localhost'"
**Solution**:
- Check if you set a password for MySQL root user
- Update PASSWORD in DBConnection.java accordingly
- Default XAMPP password is empty

### Issue: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
**Solution**:
- MySQL Connector JAR is not added to project
- Follow Step 3 to add the JDBC driver

### Issue: "Unknown database 'student_fees_db'"
**Solution**:
- Database not created yet
- Run the SQL scripts from Step 2

### Issue: Table doesn't exist
**Solution**:
- Tables not created
- Execute `02_create_tables.sql` script

---

## Customization

### Change Database Name:
1. Edit `01_create_database.sql` - change database name
2. Update DBConnection.java URL with new database name

### Change MySQL Password:
1. In phpMyAdmin: User accounts → Edit root user → Set password
2. Update DBConnection.java PASSWORD field

### Add More Sample Data:
- Edit `03_insert_sample_data.sql`
- Add more INSERT statements
- Re-run the script

---

## Database Backup

### Create Backup:
1. Open phpMyAdmin
2. Select `student_fees_db`
3. Click **Export** tab
4. Choose **Quick** export method
5. Click **Go** to download SQL file

### Restore Backup:
1. Open phpMyAdmin
2. Select `student_fees_db`
3. Click **Import** tab
4. Choose your backup SQL file
5. Click **Go**

---

## Additional Notes

- **Port**: MySQL runs on port 3306 by default
- **Character Set**: UTF-8 (utf8mb4) for international character support
- **Auto Increment**: Used for all primary keys
- **Foreign Keys**: Enforced with CASCADE delete
- **Indexes**: Added for frequently queried columns

---

## Support

If you encounter any issues:
1. Check XAMPP MySQL error logs: `C:\xampp\mysql\data\mysql_error.log`
2. Verify all SQL scripts executed without errors
3. Ensure MySQL JDBC driver is properly added to project
4. Check Java console for detailed error messages

---

## Summary Checklist

- [ ] XAMPP installed and MySQL running
- [ ] Database created using `01_create_database.sql`
- [ ] Tables created using `02_create_tables.sql`
- [ ] Sample data inserted using `03_insert_sample_data.sql`
- [ ] MySQL JDBC driver added to project
- [ ] DBConnection.java updated for MySQL
- [ ] Project cleaned and built successfully
- [ ] Application runs and connects to database
- [ ] Login tested with sample credentials

---

**Congratulations! Your Student Fee Management System is now connected to XAMPP MySQL!**
