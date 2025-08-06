@echo off
cls
echo ==========================================
echo    HOTEL BOOKING SYSTEM STARTUP
echo ==========================================
echo.

echo [INFO] Starting Hotel Booking System...
echo [INFO] Please wait while the application starts...
echo.

echo [STEP 1] Cleaning previous build and updating dependencies...
call mvn clean -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Clean failed! 
    pause
    exit /b 1
)

echo [STEP 2] Downloading dependencies (this may take a moment for first run)...
call mvn dependency:resolve -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Dependency download failed! Please check your internet connection.
    pause
    exit /b 1
)

echo [STEP 3] Compiling application...
call mvn compile -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed! Please check for errors.
    pause
    exit /b 1
)

echo [STEP 4] Starting Spring Boot application...
echo.
echo ==========================================
echo Application will be available at:
echo   http://localhost:8081
echo.
echo Login Page:
echo   http://localhost:8081/login
echo.
echo Test Credentials:
echo   admin / admin123
echo   user / user123
echo   demo / demo123
echo.
echo H2 Database Console:
echo   http://localhost:8081/h2-console
echo   JDBC URL: jdbc:h2:mem:hotelbooking
echo   Username: sa (no password)
echo.
echo Press Ctrl+C to stop the application
echo ==========================================
echo.

call mvn spring-boot:run

echo.
echo Application stopped.
pause
