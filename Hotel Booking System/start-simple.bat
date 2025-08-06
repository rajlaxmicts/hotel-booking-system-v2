@echo off
echo ================================
echo Hotel Booking System Startup
echo ================================
echo.

echo [1/3] Cleaning and compiling...
call mvn clean compile -q

if %ERRORLEVEL% neq 0 (
    echo ERROR: Compilation failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo [2/3] Compilation successful!
echo [3/3] Starting application...
echo.
echo The application will be available at: http://localhost:8081
echo Login page: http://localhost:8081/login
echo.
echo Test credentials:
echo - admin / admin123
echo - user / user123  
echo - demo / demo123
echo.
echo Press Ctrl+C to stop the application
echo ================================
echo.

call mvn spring-boot:run
