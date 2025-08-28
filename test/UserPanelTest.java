import model.User;
import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Unit tests for UserPanel class
 */
public class UserPanelTest {
    
    private UserPanel userPanel;
    private UserService mockUserService;
    
    @Before
    public void setUp() {
        // Create a mock UserService for testing
        mockUserService = new UserService();
        userPanel = new UserPanel(mockUserService);
    }
    
    @After
    public void tearDown() {
        userPanel = null;
        mockUserService = null;
    }
    
    @Test
    public void testConstructor() {
        assertNotNull("UserPanel should be created", userPanel);
        assertEquals("Layout should be BorderLayout", BorderLayout.class, userPanel.getLayout().getClass());
    }
    
    @Test
    public void testComponentInitialization() throws Exception {
        // Use reflection to access private fields
        Field userServiceField = UserPanel.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        UserService actualUserService = (UserService) userServiceField.get(userPanel);
        assertEquals("UserService should be set correctly", mockUserService, actualUserService);
        
        Field tableModelField = UserPanel.class.getDeclaredField("tableModel");
        tableModelField.setAccessible(true);
        DefaultTableModel tableModel = (DefaultTableModel) tableModelField.get(userPanel);
        assertNotNull("Table model should be initialized", tableModel);
        assertEquals("Table should have 2 columns", 2, tableModel.getColumnCount());
        assertEquals("First column should be 'Name'", "Name", tableModel.getColumnName(0));
        assertEquals("Second column should be 'Age'", "Age", tableModel.getColumnName(1));
        
        Field nameFieldField = UserPanel.class.getDeclaredField("nameField");
        nameFieldField.setAccessible(true);
        JTextField nameField = (JTextField) nameFieldField.get(userPanel);
        assertNotNull("Name field should be initialized", nameField);
        assertEquals("Name field should have 15 columns", 15, nameField.getColumns());
        
        Field ageFieldField = UserPanel.class.getDeclaredField("ageField");
        ageFieldField.setAccessible(true);
        JTextField ageField = (JTextField) ageFieldField.get(userPanel);
        assertNotNull("Age field should be initialized", ageField);
        assertEquals("Age field should have 5 columns", 5, ageField.getColumns());
    }
    
    @Test
    public void testClearFields() throws Exception {
        // Access private fields
        Field nameFieldField = UserPanel.class.getDeclaredField("nameField");
        nameFieldField.setAccessible(true);
        JTextField nameField = (JTextField) nameFieldField.get(userPanel);
        
        Field ageFieldField = UserPanel.class.getDeclaredField("ageField");
        ageFieldField.setAccessible(true);
        JTextField ageField = (JTextField) ageFieldField.get(userPanel);
        
        // Set some text in the fields
        nameField.setText("John Doe");
        ageField.setText("25");
        
        // Access and call private clearFields method
        Method clearFieldsMethod = UserPanel.class.getDeclaredMethod("clearFields");
        clearFieldsMethod.setAccessible(true);
        clearFieldsMethod.invoke(userPanel);
        
        // Verify fields are cleared
        assertEquals("Name field should be empty", "", nameField.getText());
        assertEquals("Age field should be empty", "", ageField.getText());
    }
    
    @Test
    public void testAddButtonWithValidInput() throws Exception {
        // Access private fields
        Field nameFieldField = UserPanel.class.getDeclaredField("nameField");
        nameFieldField.setAccessible(true);
        JTextField nameField = (JTextField) nameFieldField.get(userPanel);
        
        Field ageFieldField = UserPanel.class.getDeclaredField("ageField");
        ageFieldField.setAccessible(true);
        JTextField ageField = (JTextField) ageFieldField.get(userPanel);
        
        Field tableModelField = UserPanel.class.getDeclaredField("tableModel");
        tableModelField.setAccessible(true);
        DefaultTableModel tableModel = (DefaultTableModel) tableModelField.get(userPanel);
        
        // Set valid input
        nameField.setText("John Doe");
        ageField.setText("25");
        
        // Find and trigger the add button
        JButton addButton = findAddButton(userPanel);
        assertNotNull("Add button should be found", addButton);
        
        int initialRowCount = tableModel.getRowCount();
        
        // Simulate button click
        for (ActionListener listener : addButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(addButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify user was added to table
        assertEquals("Table should have one more row", initialRowCount + 1, tableModel.getRowCount());
        assertEquals("Name should be added to table", "John Doe", tableModel.getValueAt(initialRowCount, 0));
        assertEquals("Age should be added to table", 25, tableModel.getValueAt(initialRowCount, 1));
        
        // Verify fields are cleared
        assertEquals("Name field should be cleared", "", nameField.getText());
        assertEquals("Age field should be cleared", "", ageField.getText());
    }
    
    @Test
    public void testAddButtonWithInvalidAge() throws Exception {
        // Access private fields
        Field nameFieldField = UserPanel.class.getDeclaredField("nameField");
        nameFieldField.setAccessible(true);
        JTextField nameField = (JTextField) nameFieldField.get(userPanel);
        
        Field ageFieldField = UserPanel.class.getDeclaredField("ageField");
        ageFieldField.setAccessible(true);
        JTextField ageField = (JTextField) ageFieldField.get(userPanel);
        
        Field tableModelField = UserPanel.class.getDeclaredField("tableModel");
        tableModelField.setAccessible(true);
        DefaultTableModel tableModel = (DefaultTableModel) tableModelField.get(userPanel);
        
        // Set invalid input (non-numeric age)
        nameField.setText("John Doe");
        ageField.setText("invalid");
        
        JButton addButton = findAddButton(userPanel);
        int initialRowCount = tableModel.getRowCount();
        
        // Simulate button click - should handle NumberFormatException
        // In headless mode, JOptionPane will throw HeadlessException but the logic should still work
        try {
            for (ActionListener listener : addButton.getActionListeners()) {
                listener.actionPerformed(new ActionEvent(addButton, ActionEvent.ACTION_PERFORMED, ""));
            }
        } catch (java.awt.HeadlessException e) {
            // Expected in headless mode when trying to show JOptionPane
            // This confirms the error handling path was taken
        }
        
        // Verify no row was added
        assertEquals("Table should not have additional rows", initialRowCount, tableModel.getRowCount());
        
        // Verify fields are not cleared (since there was an error)
        assertEquals("Name field should still contain text", "John Doe", nameField.getText());
        assertEquals("Age field should still contain text", "invalid", ageField.getText());
    }
    
    @Test
    public void testAddButtonWithEmptyName() throws Exception {
        // Access private fields
        Field nameFieldField = UserPanel.class.getDeclaredField("nameField");
        nameFieldField.setAccessible(true);
        JTextField nameField = (JTextField) nameFieldField.get(userPanel);
        
        Field ageFieldField = UserPanel.class.getDeclaredField("ageField");
        ageFieldField.setAccessible(true);
        JTextField ageField = (JTextField) ageFieldField.get(userPanel);
        
        Field tableModelField = UserPanel.class.getDeclaredField("tableModel");
        tableModelField.setAccessible(true);
        DefaultTableModel tableModel = (DefaultTableModel) tableModelField.get(userPanel);
        
        // Set invalid input (empty name)
        nameField.setText("");
        ageField.setText("25");
        
        JButton addButton = findAddButton(userPanel);
        int initialRowCount = tableModel.getRowCount();
        
        // Simulate button click - should handle IllegalArgumentException
        // In headless mode, JOptionPane will throw HeadlessException but the logic should still work
        try {
            for (ActionListener listener : addButton.getActionListeners()) {
                listener.actionPerformed(new ActionEvent(addButton, ActionEvent.ACTION_PERFORMED, ""));
            }
        } catch (java.awt.HeadlessException e) {
            // Expected in headless mode when trying to show JOptionPane
            // This confirms the error handling path was taken
        }
        
        // Verify no row was added
        assertEquals("Table should not have additional rows", initialRowCount, tableModel.getRowCount());
    }
    
    @Test
    public void testDeleteButtonWithSelection() throws Exception {
        // Access private fields
        Field tableModelField = UserPanel.class.getDeclaredField("tableModel");
        tableModelField.setAccessible(true);
        DefaultTableModel tableModel = (DefaultTableModel) tableModelField.get(userPanel);
        
        Field userTableField = UserPanel.class.getDeclaredField("userTable");
        userTableField.setAccessible(true);
        JTable userTable = (JTable) userTableField.get(userPanel);
        
        // Add a test row
        tableModel.addRow(new Object[]{"Test User", 30});
        assertEquals("Table should have one row", 1, tableModel.getRowCount());
        
        // Select the row
        userTable.setRowSelectionInterval(0, 0);
        assertEquals("Row should be selected", 0, userTable.getSelectedRow());
        
        // Find and trigger the delete button
        JButton deleteButton = findDeleteButton(userPanel);
        assertNotNull("Delete button should be found", deleteButton);
        
        // Simulate button click
        for (ActionListener listener : deleteButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(deleteButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify row was deleted
        assertEquals("Table should be empty", 0, tableModel.getRowCount());
    }
    
    @Test
    public void testDeleteButtonWithoutSelection() throws Exception {
        // Access private fields
        Field tableModelField = UserPanel.class.getDeclaredField("tableModel");
        tableModelField.setAccessible(true);
        DefaultTableModel tableModel = (DefaultTableModel) tableModelField.get(userPanel);
        
        Field userTableField = UserPanel.class.getDeclaredField("userTable");
        userTableField.setAccessible(true);
        JTable userTable = (JTable) userTableField.get(userPanel);
        
        // Add a test row
        tableModel.addRow(new Object[]{"Test User", 30});
        assertEquals("Table should have one row", 1, tableModel.getRowCount());
        
        // Ensure no row is selected
        userTable.clearSelection();
        assertEquals("No row should be selected", -1, userTable.getSelectedRow());
        
        // Find and trigger the delete button
        JButton deleteButton = findDeleteButton(userPanel);
        
        // Simulate button click
        for (ActionListener listener : deleteButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(deleteButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify no row was deleted
        assertEquals("Table should still have one row", 1, tableModel.getRowCount());
    }
    
    /**
     * Helper method to find the Add button in the component hierarchy
     */
    private JButton findAddButton(Container container) {
        return findButtonByText(container, "Add");
    }
    
    /**
     * Helper method to find the Delete button in the component hierarchy
     */
    private JButton findDeleteButton(Container container) {
        return findButtonByText(container, "Delete");
    }
    
    /**
     * Helper method to find a button by its text in the component hierarchy
     */
    private JButton findButtonByText(Container container, String text) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (text.equals(button.getText())) {
                    return button;
                }
            } else if (component instanceof Container) {
                JButton found = findButtonByText((Container) component, text);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}