#!/bin/bash

echo "========================================"
echo "  Money Manager - Quick Start"
echo "========================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 25 or higher from: https://jdk.java.net/25/"
    exit 1
fi

# Check if JavaFX SDK exists
if [ ! -d "javafx-sdk-25.0.1/lib" ]; then
    echo "ERROR: JavaFX SDK not found!"
    echo "Please download JavaFX SDK from: https://gluonhq.com/products/javafx/"
    echo "Extract it to the project root folder"
    exit 1
fi

echo "Using Java:"
java -version
echo ""

# ========================================
#   COMPILATION
# ========================================
echo "[Step 1/2] Compiling Java files..."
echo ""

# Create bin directory
mkdir -p bin

# Compile Java files
cd src/main/java
javac --module-path "../../../javafx-sdk-25.0.1/lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing -d "../../../bin" com/utils/*.java com/models/*.java com/controllers/*.java

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Compilation failed!"
    cd ../../..
    exit 1
fi

cd ../../..

echo "Copying resources..."
mkdir -p bin/css bin/fxml bin/images bin/fonts
cp -r src/main/resources/css/* bin/css/ 2>/dev/null || true
cp -r src/main/resources/fxml/* bin/fxml/ 2>/dev/null || true
cp -r src/main/resources/images/* bin/images/ 2>/dev/null || true
cp -r src/main/resources/fonts/* bin/fonts/ 2>/dev/null || true

echo ""
echo "========================================"
echo "  Compilation Successful!"
echo "========================================"
echo ""

# ========================================
#   RUNNING APPLICATION
# ========================================
echo "[Step 2/2] Starting Money Manager..."
echo ""

java --module-path "javafx-sdk-25.0.1/lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing --enable-native-access=javafx.graphics -cp bin com.utils.App

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Application failed to start!"
    exit 1
fi

echo ""
echo "Application closed."
