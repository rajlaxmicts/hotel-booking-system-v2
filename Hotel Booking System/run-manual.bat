@echo off
echo Compiling and running Hotel Booking System...
cd /d "%~dp0"

echo Step 1: Clean and compile...
mvn clean compile

echo Step 2: Running application...
java -cp "target/classes;%USERPROFILE%\.m2\repository\org\springframework\boot\spring-boot\3.2.0\spring-boot-3.2.0.jar;%USERPROFILE%\.m2\repository\org\springframework\boot\spring-boot-autoconfigure\3.2.0\spring-boot-autoconfigure-3.2.0.jar;%USERPROFILE%\.m2\repository\org\springframework\spring-core\6.1.1\spring-core-6.1.1.jar;%USERPROFILE%\.m2\repository\org\springframework\spring-context\6.1.1\spring-context-6.1.1.jar;%USERPROFILE%\.m2\repository\org\springframework\spring-beans\6.1.1\spring-beans-6.1.1.jar;%USERPROFILE%\.m2\repository\org\springframework\spring-web\6.1.1\spring-web-6.1.1.jar;%USERPROFILE%\.m2\repository\org\springframework\spring-webmvc\6.1.1\spring-webmvc-6.1.1.jar;%USERPROFILE%\.m2\repository\org\springframework\data\spring-data-jpa\3.2.0\spring-data-jpa-3.2.0.jar;%USERPROFILE%\.m2\repository\org\hibernate\orm\hibernate-core\6.4.1.Final\hibernate-core-6.4.1.Final.jar;%USERPROFILE%\.m2\repository\com\h2database\h2\2.2.224\h2-2.2.224.jar" com.ihg.hotelbooking.HotelBookingChatbotApplication

pause
