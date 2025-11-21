@echo off
cd /d "%~dp0"
echo ========================================
echo    MY_CHATAPP - AUTOMATIC TEST RUNNER
echo ========================================
echo.

echo  Scanning for Java files...
dir /s /b *.java > files.tmp

echo  STEP 1: Compiling all Java files...
javac -cp "lib\testng.jar" -d build @files.tmp

if %errorlevel% neq 0 (
    echo  COMPILATION FAILED!
    del files.tmp
    pause
    exit /b 1
)
echo  Compilation successful!

del files.tmp
echo  Compilation successful!

echo.
echo STEP 2: Running all automatic tests...
java -cp "build;lib\testng.jar" org.testng.TestNG testng.XML
if %errorlevel% neq 0 (
    echo  SOME TESTS FAILED!
    pause
    exit /b 1
)

echo.
echo  ALL TESTS PASSED SUCCESSFULLY!
echo  Your chat app is working perfectly!
echo.
pause