@echo off
echo ==========================================
echo       QUICK START GUIDE
echo ==========================================
echo.
echo To start the Hotel Booking System:
echo.
echo 1. Open Command Prompt or PowerShell
echo 2. Navigate to the project directory:
echo    cd "c:\Users\162349\Rajlaxmi\Rajlaxmi_Latest\IHG\Vibe Coding\Hotel Booking System"
echo.
echo 3. Run ONE of these commands:
echo.
echo    Option A: Use the startup script
echo    START_APP.bat
echo.
echo    Option B: Use Maven directly  
echo    mvn spring-boot:run
echo.
echo    Option C: Clean compile then run
echo    mvn clean compile
echo    mvn spring-boot:run
echo.
echo ==========================================
echo WHAT TO EXPECT:
echo ==========================================
echo.
echo When starting, you should see:
echo   [INFO] Starting HotelBookingChatbotApplication
echo   Tomcat started on port(s): 8081 (http)
echo   ✅ Admin user created - Username: admin, Password: admin123
echo   ✅ Regular user created - Username: user, Password: user123
echo   ✅ Demo user created - Username: demo, Password: demo123
echo.
echo ==========================================
echo ACCESS THE APPLICATION:
echo ==========================================
echo.
echo Once running, open your browser and go to:
echo   http://localhost:8081
echo.
echo You should now see the register button in:
echo   - Navigation bar (top right)
echo   - Hero section (large buttons)
echo   - Fallback section (yellow "Join Now" button)
echo.
echo ==========================================
pause
