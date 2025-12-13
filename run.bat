@echo off
echo ========================================
echo   Money Manager - Starting Application
echo ========================================
echo.

REM Set Java 25 path
set "JAVA_HOME=C:\Program Files\Java\jdk-25"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Check if compiled
if not exist "bin\com" (
    echo ERROR: Application not compiled!
    echo Please run compile.bat first
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

echo Starting Money Manager...
echo.
java --module-path "javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing --enable-native-access=javafx.graphics -cp bin com.utils.App

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Application failed to start!
    pause
    exit /b 1
)
