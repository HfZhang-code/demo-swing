import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserPanel extends JPanel {
    private final UserService userService;
    private final JTable userTable;
    private final DefaultTableModel tableModel;
    private final JTextField nameField;
    private final JTextField ageField;

    public UserPanel(UserService userService) {
        this.userService = userService;
        setLayout(new BorderLayout());

        // Create table model
        String[] columns = {"Name", "Age"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);

        // Create input panel
        JPanel inputPanel = new JPanel();
        nameField = new JTextField(15);
        ageField = new JTextField(5);
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        // Add components to panel
        add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                User user = userService.getUser(name, age);
                userService.addUser(user);
                tableModel.addRow(new Object[]{user.getName(), user.getAge()});
                clearFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String name = (String) tableModel.getValueAt(selectedRow, 0);
                int age = (int) tableModel.getValueAt(selectedRow, 1);
                User user = new User(name, age);
                userService.deleteUser(user);
                tableModel.removeRow(selectedRow);
            }
        });
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("User Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new UserPanel(new UserService()));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}