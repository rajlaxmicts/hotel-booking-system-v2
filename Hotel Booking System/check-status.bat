@echo off
echo Checking if Hotel Booking System is running...
echo.

curl -s http://localhost:8081/api/auth/login > nul 2>&1

if %ERRORLEVEL% equ 0 (
    echo ✅ Application is running at http://localhost:8081
    echo ✅ You can now test the login functionality
    echo.
    echo Available pages:
    echo - Home: http://localhost:8081
    echo - Login: http://localhost:8081/login
    echo - H2 Console: http://localhost:8081/h2-console
) else (
    echo ❌ Application is NOT running
    echo.
    echo To start the application, run: start-simple.bat
    echo or manually: mvn spring-boot:run
)

echo.
pause
