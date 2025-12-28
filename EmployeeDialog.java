import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class EmployeeDialog extends JDialog {
    private DataManager dataManager;
    private Employee employee;
    private boolean saved = false;
    
    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<Department> departmentCombo;
    private JTextField roleField;
    private JTextField hourlyRateField;
    private JTextField hireDateField;
    private JPasswordField passwordField;
    
    public EmployeeDialog(JFrame parent, DataManager dataManager, Employee employee) {
        super(parent, employee == null ? "Add Employee" : "Edit Employee", true);
        this.dataManager = dataManager;
        this.employee = employee;
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(500, 500);
        setLocationRelativeTo(getParent());
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Employee ID
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        idField = new JTextField();
        if (employee != null) {
            idField.setText(String.valueOf(employee.getEmployeeId()));
            idField.setEditable(false);
        } else {
            idField.setText(String.valueOf(dataManager.getNextEmployeeId()));
            idField.setEditable(false);
        }
        panel.add(idField, gbc);
        row++;
        
        // First Name
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        firstNameField = new JTextField();
        if (employee != null) firstNameField.setText(employee.getFirstName());
        panel.add(firstNameField, gbc);
        row++;
        
        // Last Name
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        lastNameField = new JTextField();
        if (employee != null) lastNameField.setText(employee.getLastName());
        panel.add(lastNameField, gbc);
        row++;
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email (@kayan.com):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField();
        if (employee != null) {
            emailField.setText(employee.getEmail());
        } else {
            // Suggest email format for new employees
            emailField.setToolTipText("Example: firstname.lastname@kayan.com");
        }
        panel.add(emailField, gbc);
        row++;
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        phoneField = new JTextField();
        if (employee != null) phoneField.setText(employee.getPhoneNumber());
        panel.add(phoneField, gbc);
        row++;
        
        // Department
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        departmentCombo = new JComboBox<>(Department.values());
        if (employee != null) departmentCombo.setSelectedItem(employee.getDepartment());
        panel.add(departmentCombo, gbc);
        row++;
        
        // Job Role
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Job Role:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        roleField = new JTextField();
        if (employee != null) roleField.setText(employee.getJobRole());
        panel.add(roleField, gbc);
        row++;
        
        // Hourly Rate
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Hourly Rate:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        hourlyRateField = new JTextField();
        if (employee != null) hourlyRateField.setText(String.valueOf(employee.getHourlyRate()));
        panel.add(hourlyRateField, gbc);
        row++;
        
        // Hire Date
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Hire Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        hireDateField = new JTextField();
        if (employee != null) hireDateField.setText(employee.getHireDate().toString());
        else hireDateField.setText(LocalDate.now().toString());
        panel.add(hireDateField, gbc);
        row++;
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField();
        if (employee != null) passwordField.setText(employee.getPassword());
        panel.add(passwordField, gbc);
        row++;
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> saveEmployee());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);
        
        add(panel);
    }
    
    private void saveEmployee() {
        try {
            int employeeId = Integer.parseInt(idField.getText());
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            Department department = (Department) departmentCombo.getSelectedItem();
            String jobRole = roleField.getText().trim();
            double hourlyRate = Double.parseDouble(hourlyRateField.getText());
            LocalDate hireDate = LocalDate.parse(hireDateField.getText());
            String password = new String(passwordField.getPassword());
            
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || 
                phone.isEmpty() || jobRole.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }
            
            Employee emp = new Employee(employeeId, firstName, lastName, email, phone,
                                       department, jobRole, hourlyRate, hireDate, password);
            
            if (employee == null) {
                // New employee
                dataManager.addEmployee(emp);
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
            } else {
                // Update existing
                dataManager.updateEmployee(emp);
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            }
            
            saved = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                         "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}

