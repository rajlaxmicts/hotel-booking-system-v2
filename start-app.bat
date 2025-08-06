@echo off
echo Starting Hotel Booking System...
echo.

echo Compiling project...
mvn clean compile

if %ERRORLEVEL% neq 0 (
    echo.
    echo Compilation failed! Please fix the errors above.
    pause
    exit /b 1
)

echo.
echo Starting Spring Boot application...
mvn spring-boot:run
