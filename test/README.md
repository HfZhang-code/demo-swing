# UserPanel Unit Tests

This directory contains comprehensive unit tests for the `UserPanel` class, which is a Swing-based GUI component for managing user data.

## Test Structure

- `test/UserPanelTest.java` - Main test class containing 8 comprehensive test cases
- `test/lib/` - Contains JUnit 4.13.2 and Hamcrest dependencies
- `run-tests.sh` - Convenience script to compile and run tests

## Test Coverage

The test suite covers the following functionality:

### Component Initialization
- ✅ Constructor properly initializes all components
- ✅ Component hierarchy and layout are correct
- ✅ Table model is configured with correct columns
- ✅ Input fields are properly configured

### Core Functionality
- ✅ `clearFields()` method empties name and age fields
- ✅ Add button with valid input adds user to table and clears fields
- ✅ Delete button removes selected row from table
- ✅ Delete button does nothing when no row is selected

### Error Handling
- ✅ Add button handles invalid age input (NumberFormatException)
- ✅ Add button handles empty name input (IllegalArgumentException)

## Running Tests

### Using the convenience script:
```bash
./run-tests.sh
```

### Manual execution:
```bash
# Compile
javac -cp "test/lib/*:src" -d test test/UserPanelTest.java src/model/User.java src/UserService.java src/UserPanel.java

# Run tests
java -Djava.awt.headless=true -cp "test/lib/*:test:src" org.junit.runner.JUnitCore UserPanelTest
```

## Dependencies

- JUnit 4.13.2 (included in `test/lib/`)
- Hamcrest Core 1.3 (included in `test/lib/`)
- Java 8+ with Swing support

## Notes

- Tests run in headless mode to support CI/CD environments
- Error handling tests account for HeadlessException when JOptionPane dialogs would be displayed
- All tests use reflection to access private fields and methods for thorough testing
- The test suite validates both successful operations and error conditions

## Test Results

All 8 tests pass successfully, providing comprehensive coverage of the UserPanel functionality.