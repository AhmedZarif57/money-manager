#!/bin/bash

echo "========================================"
echo "  Money Manager - Compilation Script"
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

echo "Creating bin directory..."
mkdir -p bin

echo "Compiling Java files..."
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

cp -r src/main/resources/css/* bin/css/ 2>/dev/null
cp -r src/main/resources/fxml/* bin/fxml/ 2>/dev/null
cp -r src/main/resources/images/* bin/images/ 2>/dev/null
cp -r src/main/resources/fonts/* bin/fonts/ 2>/dev/null

echo ""
echo "========================================"
echo "  Compilation Successful!"
echo "========================================"
echo ""
echo "You can now run the application using: ./run.sh"
