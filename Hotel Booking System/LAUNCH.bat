@echo off
title Hotel Booking System - Starting...
cls
echo.
echo  ██╗  ██╗ ██████╗ ████████╗███████╗██╗         
echo  ██║  ██║██╔═══██╗╚══██╔══╝██╔════╝██║         
echo  ███████║██║   ██║   ██║   █████╗  ██║         
echo  ██╔══██║██║   ██║   ██║   ██╔══╝  ██║         
echo  ██║  ██║╚██████╔╝   ██║   ███████╗███████╗    
echo  ╚═╝  ╚═╝ ╚═════╝    ╚═╝   ╚══════╝╚══════╝    
echo.
echo  ██████╗  ██████╗  ██████╗ ██╗  ██╗██╗███╗   ██╗ ██████╗ 
echo  ██╔══██╗██╔═══██╗██╔═══██╗██║ ██╔╝██║████╗  ██║██╔════╝ 
echo  ██████╔╝██║   ██║██║   ██║█████╔╝ ██║██╔██╗ ██║██║  ███╗
echo  ██╔══██╗██║   ██║██║   ██║██╔═██╗ ██║██║╚██╗██║██║   ██║
echo  ██████╔╝╚██████╔╝╚██████╔╝██║  ██╗██║██║ ╚████║╚██████╔╝
echo  ╚═════╝  ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝ 
echo.
echo ================================================================
echo                    HOTEL BOOKING SYSTEM
echo                     with AI Chatbot
echo ================================================================
echo.
echo [STARTING] Please wait while the application starts...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo [STEP 1/4] Cleaning previous build...
call mvn clean -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Maven clean failed!
    pause
    exit /b 1
)

echo [STEP 2/4] Resolving dependencies...
call mvn dependency:resolve -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Dependency resolution failed!
    echo Check your internet connection
    pause
    exit /b 1
)

echo [STEP 3/4] Compiling application...
call mvn compile -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed!
    echo Check console output above for details
    pause
    exit /b 1
)

echo [STEP 4/4] Starting Spring Boot application...
echo.
echo ================================================================
echo                    APPLICATION READY!
echo ================================================================
echo.
echo  🌐 Home Page:      http://localhost:8081
echo  🔐 Login Page:     http://localhost:8081/login
echo  📝 Register Page:  http://localhost:8081/register
echo  🤖 Chatbot:       http://localhost:8081/chatbot
echo  🗄️  H2 Console:    http://localhost:8081/h2-console
echo.
echo ================================================================
echo                    TEST CREDENTIALS
echo ================================================================
echo.
echo  👤 Admin:    admin / admin123
echo  👤 User:     user / user123
echo  👤 Demo:     demo / demo123
echo.
echo ================================================================
echo.
echo 🎯 REGISTER BUTTON LOCATIONS (on home page):
echo   • Navigation bar (top right): "Register" link
echo   • Hero section: "Create Account" button
echo   • Fallback section: "Join Now - Free Registration" button
echo.
echo ⚠️  Press Ctrl+C to stop the application
echo ================================================================
echo.

call mvn spring-boot:run

echo.
echo ================================================================
echo Application has stopped.
echo ================================================================
pause
