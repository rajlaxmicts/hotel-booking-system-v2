# Hotel Booking System Launcher
Write-Host "================================================================" -ForegroundColor Blue
Write-Host "              HOTEL BOOKING SYSTEM LAUNCHER" -ForegroundColor Blue
Write-Host "================================================================" -ForegroundColor Blue
Write-Host ""

Write-Host "🚀 Starting Hotel Booking System..." -ForegroundColor Yellow
Write-Host ""

# Check Java
try {
    $javaVersion = & java -version 2>&1 | Select-String "version" | Select-Object -First 1
    Write-Host "✅ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Java not found! Please install Java 17+" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Check Maven
try {
    $mavenVersion = & mvn -version 2>&1 | Select-String "Apache Maven" | Select-Object -First 1
    Write-Host "✅ Maven found: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Maven not found! Please install Maven 3.6+" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "[1/4] Cleaning project..." -ForegroundColor Cyan
& mvn clean -q
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Clean failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[2/4] Resolving dependencies..." -ForegroundColor Cyan
& mvn dependency:resolve -q
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Dependency resolution failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[3/4] Compiling..." -ForegroundColor Cyan
& mvn compile -q
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Compilation failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[4/4] Starting application..." -ForegroundColor Cyan
Write-Host ""
Write-Host "================================================================" -ForegroundColor Green
Write-Host "                  🎉 APPLICATION READY! 🎉" -ForegroundColor Green
Write-Host "================================================================" -ForegroundColor Green
Write-Host ""
Write-Host "📍 QUICK ACCESS LINKS:" -ForegroundColor White
Write-Host "   🏠 Home:      " -NoNewline; Write-Host "http://localhost:8081" -ForegroundColor Blue
Write-Host "   🔐 Login:     " -NoNewline; Write-Host "http://localhost:8081/login" -ForegroundColor Blue
Write-Host "   📝 Register:  " -NoNewline; Write-Host "http://localhost:8081/register" -ForegroundColor Blue
Write-Host "   🤖 Chatbot:   " -NoNewline; Write-Host "http://localhost:8081/chatbot" -ForegroundColor Blue
Write-Host ""
Write-Host "🔑 TEST CREDENTIALS:" -ForegroundColor White
Write-Host "   👤 admin / admin123" -ForegroundColor Gray
Write-Host "   👤 user / user123" -ForegroundColor Gray
Write-Host "   👤 demo / demo123" -ForegroundColor Gray
Write-Host ""
Write-Host "🎯 REGISTER BUTTON LOCATIONS:" -ForegroundColor White
Write-Host "   • Navigation bar: 'Register' link" -ForegroundColor Gray
Write-Host "   • Hero section: 'Create Account' button" -ForegroundColor Gray
Write-Host "   • Fallback: 'Join Now - Free Registration'" -ForegroundColor Gray
Write-Host ""
Write-Host "⚠️  Press Ctrl+C to stop the server" -ForegroundColor Yellow
Write-Host "================================================================" -ForegroundColor Green
Write-Host ""

# Start the application
& mvn spring-boot:run

Write-Host ""
Write-Host "================================================================" -ForegroundColor Blue
Write-Host "Application stopped." -ForegroundColor Blue
Write-Host "================================================================" -ForegroundColor Blue
Read-Host "Press Enter to exit"
