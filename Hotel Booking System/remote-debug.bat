@echo off
REM Remote debug script for Windows
echo Starting Spring Boot application in debug mode...
echo Debug port: 5005
echo Web server: http://localhost:8081
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" -Dspring-boot.run.arguments="--spring.profiles.active=debug"
