<<<<<<< HEAD
# Student Fee Management System

A comprehensive Java-based desktop application for managing student fees, course enrollments, and payment tracking in educational institutions.


![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=flat&logo=apache-netbeans-ide&logoColor=white)

---

## 📋 Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [System Requirements](#system-requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Database Schema](#database-schema)
- [Documentation](#documentation)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)

---

## ✨ Features

### 👨‍💼 Registrar Portal
- ✅ Generate unique registration numbers
- ✅ Register new students with courses
- ✅ Manage course catalog (Add/Update/Delete)
- ✅ View enrolled students list
- ✅ Delete students with cascade removal
- ✅ Set student passwords

### 💰 Accountant Portal
- ✅ View all students with fee summaries
- ✅ Color-coded payment status (Paid/Partial/Unpaid)
- ✅ View detailed payment history per student
- ✅ Track total fees, paid amounts, and balances
- ✅ Financial reporting

### 🎓 Student Portal
- ✅ Secure login with registration number
- ✅ View enrolled courses and fees
- ✅ Make payments via MOMO or BK
- ✅ View payment history
- ✅ Check fee status in real-time

---

## 🛠️ Technologies

| Technology | Purpose |
|------------|---------|
| **Java** | Core application language |
| **Java Swing** | GUI framework |
| **MySQL** | Database management |
| **JDBC** | Database connectivity |
| **XAMPP** | Local MySQL server |
| **NetBeans IDE** | Development environment |
| **Git/GitHub** | Version control |

---

## 💻 System Requirements

### Software:
- Java JDK 8 or higher
- NetBeans IDE 12.0+
- XAMPP (MySQL 5.7+)
- MySQL Connector/J 8.x
- Git (for version control)

### Hardware:
- Processor: Intel Core i3 or equivalent
- RAM: 4GB minimum (8GB recommended)
- Storage: 500MB free space
- OS: Windows 10/11, macOS, or Linux

---

## 📦 Installation

### Step 1: Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/student-fee-management-system.git
cd student-fee-management-system
```

### Step 2: Setup Database

1. **Start XAMPP:**
   - Open XAMPP Control Panel
   - Start MySQL service

2. **Create Database:**
   - Open phpMyAdmin: `http://localhost/phpmyadmin`
   - Click "SQL" tab
   - Run scripts in order:
     ```
     database_setup/01_create_database.sql
     database_setup/02_create_tables.sql
     database_setup/07_fresh_start_clean_data.sql
     ```

### Step 3: Configure Project

1. **Open in NetBeans:**
   - File → Open Project
   - Select project folder

2. **Add MySQL Connector:**
   - Right-click project → Properties
   - Libraries → Add JAR/Folder
   - Add `mysql-connector-j-8.x.x.jar`

3. **Verify Database Connection:**
   - Check `src/database/DBConnection.java`
   - Default settings:
     ```java
     URL: jdbc:mysql://localhost:3306/student_fees_db
     Username: root
     Password: (empty)
     ```

### Step 4: Build and Run

```bash
# In NetBeans:
Shift + F11  (Clean and Build)
F6           (Run Project)
```

---

## 🚀 Usage

### Login Credentials

| Role | Username | Password |
|------|----------|----------|
| **Registrar** | `registrar` | `reg123` |
| **Accountant** | `accountant` | `acc123` |
| **Student** | `2025001` | `pass123` |

### Registrar Workflow

1. **Register Student:**
   - Click "Generate" for reg number
   - Enter student name
   - Select program (SOFTWARE ENGINEERING, INFO MANAGEMENT, or NETWORKING)
   - Set password
   - Select courses
   - Click "Register"

2. **Manage Courses:**
   - Go to "Course Catalog" tab
   - Add, update, or delete courses
   - Set prices in RWF

3. **Delete Student:**
   - Go to "Enrolled List" tab
   - Select student
   - Click "Delete Selected Student"
   - Confirm deletion

### Student Workflow

1. **Login:**
   - Select "Student" from dropdown
   - Enter registration number
   - Enter password

2. **Make Payment:**
   - Go to "Pay" tab
   - Select payment method (MOMO or BK)
   - Enter amount
   - Add note/reference
   - Click "Pay"

3. **View Status:**
   - Check "Status" tab for payment summary
   - View "Payments" tab for history

### Accountant Workflow

1. **View Students:**
   - Login to see all registered students
   - Colors indicate status:
     - 🟢 Green: Fully paid
     - 🟠 Orange: Partially paid
     - 🔴 Red: Not paid

2. **View Details:**
   - Click on any student
   - See payment history on right panel
   - View payment methods and dates

---

## 🗄️ Database Schema

### Tables (5):

```sql
fees_users          -- Staff accounts (Registrar, Accountant)
fees_students       -- Student records
fees_courses        -- Course catalog
fees_enrollments    -- Student-course relationships
fees_payments       -- Payment transactions
```

### Key Relationships:

```
fees_students (1) ──→ (N) fees_enrollments
fees_courses (1)  ──→ (N) fees_enrollments
fees_students (1) ──→ (N) fees_payments
```

### Features:
- ✅ Foreign key constraints with CASCADE DELETE
- ✅ AUTO_INCREMENT for primary keys
- ✅ Indexes on frequently queried columns
- ✅ BigDecimal for precise money calculations

---

## 📚 Documentation

| Document | Description |
|----------|-------------|
| [SETUP_INSTRUCTIONS.md](database_setup/SETUP_INSTRUCTIONS.md) | Detailed setup guide |
| [CRITERION_3_CODE_QUALITY.md](CRITERION_3_CODE_QUALITY.md) | Code quality analysis |
| [CRITERION_4_VERSION_CONTROL.md](CRITERION_4_VERSION_CONTROL.md) | Version control guide |
| [NEW_FEATURES_ADDED.md](NEW_FEATURES_ADDED.md) | Recent features |
| [QUICK_ACTION_GUIDE.txt](QUICK_ACTION_GUIDE.txt) | Quick reference |
| [BUILD_AND_RUN.txt](BUILD_AND_RUN.txt) | Build instructions |

---

## 📸 Screenshots

### Login Screen
![Login Screen](screenshots/login.png)

### Registrar Dashboard
![Registrar Dashboard](screenshots/registrar.png)

### Student Portal
![Student Portal](screenshots/student.png)

### Accountant Dashboard
![Accountant Dashboard](screenshots/accountant.png)

---

## 🏗️ Project Structure

```
student-fee-management-system/
├── src/
│   ├── database/              # Data Access Layer
│   │   ├── DBConnection.java
│   │   ├── RegistrarDAO.java
│   │   ├── StudentPortalDAO.java
│   │   └── FeeAccountDAO.java
│   ├── models/                # Data Models
│   │   ├── Course.java
│   │   ├── Student.java
│   │   ├── Payment.java
│   │   └── FeeSummary.java
│   └── ui/                    # User Interface
│       ├── LoginScreen.java
│       ├── RegistrarDashboard.java
│       ├── StudentDashboard.java
│       └── AccountantDashboard.java
├── database_setup/            # SQL Scripts
│   ├── 01_create_database.sql
│   ├── 02_create_tables.sql
│   └── 07_fresh_start_clean_data.sql
├── docs/                      # Documentation
├── lib/                       # External Libraries
└── README.md                  # This file
```

---

## 🎨 Design Patterns

### Singleton Pattern
- **DBConnection:** Single database connection instance

### DAO Pattern
- **RegistrarDAO:** Registrar operations
- **StudentPortalDAO:** Student operations
- **FeeAccountDAO:** Accountant operations

### MVC-like Architecture
- **Models:** Data structures
- **Views:** UI components
- **Controllers:** DAO classes

---

## 🔒 Security Features

- ✅ **SQL Injection Prevention:** PreparedStatements
- ✅ **Password Protection:** Secure authentication
- ✅ **Input Validation:** All user inputs validated
- ✅ **Cascade Delete:** Maintains referential integrity
- ✅ **Role-based Access:** Separate portals for each role

---

## 🧪 Testing

### Test Credentials:
```
Registrar:  registrar / reg123
Accountant: accountant / acc123
Student:    2025001 / pass123
```

### Test Scenarios:
1. ✅ Register new student
2. ✅ Enroll in courses
3. ✅ Make payment (MOMO/BK)
4. ✅ View payment history
5. ✅ Delete student
6. ✅ Manage courses

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**[Your Name]**
- Student ID: [Your ID]
- Email: [Your Email]
- GitHub: [@yourusername](https://github.com/yourusername)

---

## 🙏 Acknowledgments

- Lecturer: [Lecturer Name]
- Course: [Course Name]
- Institution: [Institution Name]
- Year: 2025

---

## 📞 Support

For support, email [your.email@example.com] or open an issue on GitHub.

---

## 🔄 Version History

- **v2.0** (Dec 2025) - Added delete function, payment methods, MySQL migration
- **v1.0** (Nov 2025) - Initial release with Oracle database

---

**⭐ If you find this project helpful, please give it a star!**

---

*Last Updated: December 18, 2025*
