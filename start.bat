@echo off
echo ========================================
echo   Money Manager - Quick Start
echo ========================================
echo.

REM Set Java 25 path
set "JAVA_HOME=C:\Program Files\Java\jdk-25"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 25 from: https://jdk.java.net/25/
    pause
    exit /b 1
)

REM Check if JavaFX SDK exists
if not exist "javafx-sdk-25.0.1\lib" (
    echo ERROR: JavaFX SDK not found!
    echo Please download JavaFX SDK from: https://gluonhq.com/products/javafx/
    echo Extract it to the project root folder
    pause
    exit /b 1
)

echo Using Java 25:
java -version
echo.

REM ========================================
REM   COMPILATION
REM ========================================
echo [Step 1/2] Compiling Java files...
echo.

REM Create bin directory
if not exist "bin" mkdir bin

REM Compile Java files
cd src\main\java
javac --module-path "..\..\..\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing -d "..\..\..\bin" com\utils\*.java com\models\*.java com\controllers\*.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    cd ..\..\..
    pause
    exit /b 1
)

cd ..\..\..

echo Copying resources...
if not exist "bin\css" mkdir bin\css
if not exist "bin\fxml" mkdir bin\fxml
if not exist "bin\images" mkdir bin\images
if not exist "bin\fonts" mkdir bin\fonts

xcopy /E /I /Y "src\main\resources\css\*" "bin\css\" >nul
xcopy /E /I /Y "src\main\resources\fxml\*" "bin\fxml\" >nul
xcopy /E /I /Y "src\main\resources\images\*" "bin\images\" >nul
xcopy /E /I /Y "src\main\resources\fonts\*" "bin\fonts\" >nul

echo.
echo ========================================
echo   Compilation Successful!
echo ========================================
echo.

REM ========================================
REM   RUNNING APPLICATION
REM ========================================
echo [Step 2/2] Starting Money Manager...
echo.

java --module-path "javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing --enable-native-access=javafx.graphics -cp bin com.utils.App

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Application failed to start!
    pause
    exit /b 1
)

REM Application closed successfully
echo.
echo Application closed.
pause
