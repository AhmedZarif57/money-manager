#!/bin/bash

echo "========================================"
echo "  Money Manager - Starting Application"
echo "========================================"
echo ""

# Check if compiled
if [ ! -d "bin/com" ]; then
    echo "ERROR: Application not compiled!"
    echo "Please run ./compile.sh first"
    exit 1
fi

# Check if JavaFX SDK exists
if [ ! -d "javafx-sdk-25.0.1/lib" ]; then
    echo "ERROR: JavaFX SDK not found!"
    echo "Please download JavaFX SDK from: https://gluonhq.com/products/javafx/"
    echo "Extract it to the project root folder"
    exit 1
fi

echo "Starting Money Manager..."
echo ""
java --module-path "javafx-sdk-25.0.1/lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing --enable-native-access=javafx.graphics -cp bin com.utils.App

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Application failed to start!"
    exit 1
fi
