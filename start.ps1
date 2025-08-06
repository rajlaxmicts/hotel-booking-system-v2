# Hotel Booking System Startup Script
Write-Host "================================" -ForegroundColor Blue
Write-Host "Hotel Booking System Startup" -ForegroundColor Blue
Write-Host "================================" -ForegroundColor Blue
Write-Host ""

Write-Host "[1/3] Checking Java and Maven..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    $mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven"
    Write-Host "✅ Java: $javaVersion" -ForegroundColor Green
    Write-Host "✅ Maven: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Error: Java or Maven not found in PATH" -ForegroundColor Red
    Write-Host "Please install Java 17+ and Maven 3.6+" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "[2/3] Compiling project..." -ForegroundColor Yellow
$compileResult = & mvn clean compile -q
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Compilation failed!" -ForegroundColor Red
    Write-Host "Please check the error messages above." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}
Write-Host "✅ Compilation successful!" -ForegroundColor Green

Write-Host ""
Write-Host "[3/3] Starting application..." -ForegroundColor Yellow
Write-Host ""
Write-Host "🌐 Application will be available at: http://localhost:8081" -ForegroundColor Cyan
Write-Host "🔐 Login page: http://localhost:8081/login" -ForegroundColor Cyan
Write-Host ""
Write-Host "📋 Test credentials:" -ForegroundColor White
Write-Host "   - admin / admin123" -ForegroundColor White
Write-Host "   - user / user123" -ForegroundColor White  
Write-Host "   - demo / demo123" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Yellow
Write-Host "================================" -ForegroundColor Blue
Write-Host ""

# Start the application
& mvn spring-boot:run
