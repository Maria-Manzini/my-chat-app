@echo off
echo ========================================
echo    MY_CHATAPP - AUTOMATIC TEST RUNNER
echo ========================================
echo.

echo 🔄 STEP 1: Compiling all Java files...
javac -cp "lib\testng.jar" -d . my\chatapp\MYCHATAPP.java my\chatapp\User.java my\chatapp\Registration.java my\chatapp\Login.java my\chatapp\Message.java my\chatapp\MessageService.java my\chatapp\RegistrationLoginTest.java my\chatapp\MessageTest.java

if %errorlevel% neq 0 (
    echo ❌ COMPILATION FAILED! Check your Java code.
    pause
    exit /b 1
)
echo ✅ Compilation successful!

echo.
echo 🧪 STEP 2: Running all automatic tests...
java -cp ".;lib\testng.jar" org.testng.TestNG testng.xml
if %errorlevel% neq 0 (
    echo ❌ SOME TESTS FAILED!
    pause
    exit /b 1
)

echo.
echo 🎉 ALL TESTS PASSED SUCCESSFULLY!
echo ✅ Your chat app is working perfectly!
echo.
pause