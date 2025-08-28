#!/bin/bash
# Test runner script for UserPanel tests

# Set the directory to the project root
cd "$(dirname "$0")"

echo "Compiling Java source files..."
javac -cp "test/lib/*:src" -d test test/UserPanelTest.java src/model/User.java src/UserService.java src/UserPanel.java

if [ $? -eq 0 ]; then
    echo "Compilation successful. Running tests..."
    java -Djava.awt.headless=true -cp "test/lib/*:test:src" org.junit.runner.JUnitCore UserPanelTest
else
    echo "Compilation failed!"
    exit 1
fi