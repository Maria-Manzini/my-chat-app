@echo off
echo Starting Automatic Tests...
cd /d "%~dp0"
javac -cp "lib\testng.jar" my\chatapp\*.java
java -cp ".;lib\testng.jar" org.testng.TestNG testng.xml
pause