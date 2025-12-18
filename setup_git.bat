@echo off
echo ================================================================================
echo                    GIT SETUP - Student Fee Management System
echo ================================================================================
echo.

REM Check if git is installed
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Git is not installed!
    echo Please download and install Git from: https://git-scm.com/download/win
    pause
    exit /b 1
)

echo Git is installed. Proceeding with setup...
echo.

REM Initialize git repository
echo [1/6] Initializing Git repository...
git init
echo.

REM Create .gitignore
echo [2/6] Creating .gitignore file...
(
echo # NetBeans specific
echo nbproject/private/
echo build/
echo nbbuild/
echo dist/
echo nbdist/
echo .nb-gradle/
echo.
echo # Compiled class files
echo *.class
echo.
echo # Log files
echo *.log
echo.
echo # Package Files
echo *.jar
echo *.war
echo *.nar
echo *.ear
echo *.zip
echo *.tar.gz
echo *.rar
echo.
echo # Virtual machine crash logs
echo hs_err_pid*
echo.
echo # IDE specific
echo .idea/
echo *.iml
echo .vscode/
echo.
echo # OS specific
echo .DS_Store
echo Thumbs.db
echo.
echo # Database
echo *.db
echo *.sqlite
echo.
echo # Temporary files
echo *.tmp
echo *.bak
echo *.swp
echo *~.nib
) > .gitignore
echo .gitignore created!
echo.

REM Add all files
echo [3/6] Adding all files to Git...
git add .
echo.

REM Initial commit
echo [4/6] Creating initial commit...
git commit -m "Initial commit: Student Fee Management System with MySQL"
echo.

REM Prompt for GitHub repository URL
echo [5/6] GitHub Repository Setup
echo.
echo Please create a repository on GitHub first:
echo 1. Go to https://github.com/new
echo 2. Name: student-fee-management-system
echo 3. Create repository
echo.
set /p GITHUB_URL="Enter your GitHub repository URL (e.g., https://github.com/username/repo.git): "

if "%GITHUB_URL%"=="" (
    echo No URL provided. Skipping remote setup.
    echo You can add it later with: git remote add origin [URL]
) else (
    echo Adding remote origin...
    git remote add origin %GITHUB_URL%
    echo.
    
    echo [6/6] Pushing to GitHub...
    git branch -M main
    git push -u origin main
    echo.
)

echo ================================================================================
echo                            SETUP COMPLETE!
echo ================================================================================
echo.
echo Git repository initialized successfully!
echo.
echo Next steps:
echo 1. Your code is now version controlled
echo 2. Make changes to your code
echo 3. Use these commands:
echo    - git add .                  (stage changes)
echo    - git commit -m "message"    (commit changes)
echo    - git push                   (push to GitHub)
echo.
echo For SVN setup, see CRITERION_4_VERSION_CONTROL.md
echo.
pause
