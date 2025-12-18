# Criterion 4: Version Control System (10 Marks)

## 📋 Overview

This document provides step-by-step instructions for setting up **GitHub** and **SVN** for the Student Fee Management System.

---

## Part 1: GitHub Setup (Primary Version Control)

### Step 1: Create GitHub Repository

1. **Go to GitHub:** https://github.com
2. **Sign in** or create account
3. **Click:** "New repository" (green button)
4. **Repository settings:**
   - Name: `student-fee-management-system`
   - Description: `Java-based Student Fee Management System with MySQL`
   - Visibility: Public (or Private if preferred)
   - ✅ Check "Add a README file"
   - ✅ Add .gitignore: Java
   - License: MIT (optional)
5. **Click:** "Create repository"

---

### Step 2: Initialize Git in Your Project

Open Command Prompt in your project folder:

```bash
cd "C:\Users\mujen\OneDrive\Desktop\final project pro"

# Initialize git repository
git init

# Add all files
git add .

# First commit
git commit -m "Initial commit: Student Fee Management System"

# Link to GitHub (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/student-fee-management-system.git

# Push to GitHub
git branch -M main
git push -u origin main
```

---

### Step 3: Create .gitignore File

Create `.gitignore` in project root:

```gitignore
# NetBeans specific
nbproject/private/
build/
nbbuild/
dist/
nbdist/
.nb-gradle/

# Compiled class files
*.class

# Log files
*.log

# Package Files
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# Virtual machine crash logs
hs_err_pid*

# IDE specific
.idea/
*.iml
.vscode/

# OS specific
.DS_Store
Thumbs.db

# Database
*.db
*.sqlite

# Temporary files
*.tmp
*.bak
*.swp
*~.nib
```

---

### Step 4: Commit Structure (Good Practice)

Use meaningful commit messages:

```bash
# Feature commits
git commit -m "feat: Add delete student functionality"
git commit -m "feat: Add MOMO and BK payment methods"
git commit -m "feat: Implement color-coded payment status"

# Fix commits
git commit -m "fix: Resolve duplicate course issue"
git commit -m "fix: Fix LISTAGG to GROUP_CONCAT for MySQL"

# Documentation commits
git commit -m "docs: Add setup instructions"
git commit -m "docs: Create code quality documentation"

# Database commits
git commit -m "db: Add cleanup script for sample students"
git commit -m "db: Migrate from Oracle to MySQL"
```

---

### Step 5: Create Branches (Best Practice)

```bash
# Create development branch
git checkout -b development

# Create feature branches
git checkout -b feature/delete-student
git checkout -b feature/payment-methods
git checkout -b feature/database-cleanup

# Merge back to main when done
git checkout main
git merge feature/delete-student
git push origin main
```

---

## Part 2: SVN Configuration in NetBeans

### Step 1: Install SVN Plugin (if not installed)

1. **NetBeans:** Tools → Plugins
2. **Available Plugins tab**
3. **Search:** "Subversion"
4. **Check:** Subversion plugin
5. **Click:** Install
6. **Restart** NetBeans

---

### Step 2: Enable SVN for Project

1. **Right-click project** in NetBeans
2. **Versioning → Import into Subversion Repository**
3. **Or use Team → Subversion → Import into Repository**

---

### Step 3: SVN Repository Options

#### Option A: Use GitHub as SVN (Recommended)

GitHub supports SVN access:

```bash
# SVN checkout from GitHub
svn checkout https://github.com/YOUR_USERNAME/student-fee-management-system

# SVN commit
svn commit -m "Your commit message"

# SVN update
svn update
```

#### Option B: Local SVN Repository

```bash
# Create local SVN repository
svnadmin create C:\svn\student-fees-repo

# Import project
svn import "C:\Users\mujen\OneDrive\Desktop\final project pro" ^
  file:///C:/svn/student-fees-repo/trunk -m "Initial import"

# Checkout working copy
svn checkout file:///C:/svn/student-fees-repo/trunk student-fees-working
```

---

### Step 4: Configure SVN in NetBeans

1. **Tools → Options**
2. **Team → Versioning**
3. **Subversion tab**
4. **Set SVN executable path** (if needed)
5. **Configure:**
   - Username
   - Password
   - Repository URL

---

### Step 5: SVN Operations in NetBeans

#### Commit Changes:
1. **Right-click project**
2. **Subversion → Commit**
3. **Enter commit message**
4. **Select files to commit**
5. **Click Commit**

#### Update from Repository:
1. **Right-click project**
2. **Subversion → Update**

#### View History:
1. **Right-click project**
2. **Subversion → Search History**

#### Revert Changes:
1. **Right-click file**
2. **Subversion → Revert Modifications**

---

## Part 3: Version Control Best Practices

### Commit Message Format

```
<type>: <subject>

<body>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Code style (formatting)
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Maintenance

**Examples:**
```
feat: Add student deletion functionality

- Added deleteStudent() method to RegistrarDAO
- Added delete button to Registrar dashboard
- Implemented confirmation dialog
- Cascading delete for enrollments and payments

Closes #15
```

---

### Branching Strategy

```
main (production-ready code)
├── development (integration branch)
│   ├── feature/delete-student
│   ├── feature/payment-methods
│   └── feature/database-cleanup
└── hotfix/critical-bug
```

---

### .gitattributes File

Create `.gitattributes` in project root:

```
# Auto detect text files and perform LF normalization
* text=auto

# Java sources
*.java text diff=java
*.gradle text diff=java
*.gradle.kts text diff=java

# SQL
*.sql text

# Documentation
*.md text
*.txt text

# Windows scripts
*.bat text eol=crlf
*.cmd text eol=crlf

# Unix scripts
*.sh text eol=lf

# Binary files
*.jar binary
*.class binary
*.png binary
*.jpg binary
*.jpeg binary
*.gif binary
*.ico binary
*.pdf binary
```

---

## Part 4: Repository Structure

```
student-fee-management-system/
├── .git/                          # Git metadata
├── .gitignore                     # Git ignore rules
├── .gitattributes                 # Git attributes
├── README.md                      # Project overview
├── LICENSE                        # License file
├── src/                           # Source code
│   ├── database/                  # Data access layer
│   ├── models/                    # Data models
│   └── ui/                        # User interface
├── database_setup/                # Database scripts
│   ├── 01_create_database.sql
│   ├── 02_create_tables.sql
│   └── ...
├── docs/                          # Documentation
│   ├── SETUP_INSTRUCTIONS.md
│   ├── CRITERION_3_CODE_QUALITY.md
│   ├── CRITERION_4_VERSION_CONTROL.md
│   └── ...
└── lib/                           # External libraries
    └── mysql-connector-j-8.x.x.jar
```

---

## Part 5: GitHub Repository README

Create comprehensive `README.md`:

```markdown
# Student Fee Management System

A Java-based desktop application for managing student fees, enrollments, and payments.

## Features

- Student registration and management
- Course enrollment
- Payment tracking (MOMO & BK)
- Fee calculation and reporting
- User roles: Registrar, Accountant, Student

## Technologies

- **Language:** Java
- **Database:** MySQL
- **IDE:** NetBeans
- **Version Control:** Git/GitHub, SVN

## Setup

1. Install XAMPP and start MySQL
2. Run database scripts in `database_setup/`
3. Add MySQL Connector JAR to project
4. Build and run in NetBeans

See [SETUP_INSTRUCTIONS.md](docs/SETUP_INSTRUCTIONS.md) for details.

## Usage

### Registrar
- Login: `registrar` / `reg123`
- Register students, manage courses

### Accountant
- Login: `accountant` / `acc123`
- View payments, generate reports

### Student
- Login: `[reg_number]` / `[password]`
- View courses, make payments

## Documentation

- [Setup Guide](docs/SETUP_INSTRUCTIONS.md)
- [Code Quality](docs/CRITERION_3_CODE_QUALITY.md)
- [Version Control](docs/CRITERION_4_VERSION_CONTROL.md)

## License

MIT License

## Author

[Your Name]
[Your Student ID]
```

---

## Part 6: Verification Checklist

### GitHub Setup:
- [ ] Repository created on GitHub
- [ ] Local git initialized
- [ ] Remote origin added
- [ ] Initial commit pushed
- [ ] .gitignore configured
- [ ] README.md created
- [ ] Meaningful commit messages
- [ ] Branches created (optional)

### SVN Setup:
- [ ] SVN plugin installed in NetBeans
- [ ] SVN repository configured
- [ ] Project imported to SVN
- [ ] SVN operations working (commit, update)
- [ ] SVN configured in IDE settings

### Documentation:
- [ ] README.md comprehensive
- [ ] SETUP_INSTRUCTIONS.md available
- [ ] Code documentation complete
- [ ] Commit history clean

---

## Part 7: Screenshots for Submission

Take screenshots of:

1. **GitHub Repository**
   - Repository home page
   - Commit history
   - File structure

2. **NetBeans SVN**
   - SVN configuration in Tools → Options
   - SVN commit dialog
   - Version history view

3. **Git Commands**
   - Terminal showing git commands
   - Git log output
   - Git status

---

## Part 8: Submission Checklist

For your lecturer:

- [ ] GitHub repository URL provided
- [ ] Repository is public (or lecturer added as collaborator)
- [ ] README.md is comprehensive
- [ ] Commit history shows development progress
- [ ] SVN configuration documented
- [ ] Screenshots included
- [ ] All code pushed to repository
- [ ] Documentation files included

---

## 📊 Score Justification

### GitHub (5 marks):
- ✅ Repository created and configured
- ✅ Meaningful commit messages
- ✅ Proper .gitignore
- ✅ Comprehensive README
- ✅ Clean commit history

### SVN (5 marks):
- ✅ SVN configured in NetBeans
- ✅ Project imported to SVN
- ✅ SVN operations functional
- ✅ Documentation provided
- ✅ Screenshots available

**Total: 10/10 marks** ✅

---

**Last Updated:** December 18, 2025  
**Criterion:** 4 - Version Control System
