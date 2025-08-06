@echo off
echo Checking Hotel Booking System status...
echo.

echo Testing connection to localhost:8081...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:8081 2>nul

if %ERRORLEVEL% equ 0 (
    echo.
    echo ✅ SUCCESS: Application is running!
    echo.
    echo You can now access:
    echo   🏠 Home Page: http://localhost:8081
    echo   🔐 Login Page: http://localhost:8081/login
    echo   🗄️ H2 Console: http://localhost:8081/h2-console
    echo.
    echo Test with these credentials:
    echo   👤 admin / admin123
    echo   👤 user / user123
    echo   👤 demo / demo123
) else (
    echo.
    echo ❌ FAILED: Application is not running
    echo.
    echo To start the application:
    echo   1. Double-click START_APP.bat
    echo   2. Or run: mvn spring-boot:run
    echo   3. Wait for "Tomcat started on port(s): 8081"
)

echo.
pause
