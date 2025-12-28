import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private DataManager dataManager;
    
    public LoginFrame(DataManager dataManager) {
        this.dataManager = dataManager;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Kayan - Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Kayan - Employee Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Employee ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Employee ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        idField = new JTextField(15);
        formPanel.add(idField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(new LoginActionListener());
        formPanel.add(loginButton, gbc);
        
        // Info label
        gbc.gridy = 3;
        JLabel infoLabel = new JLabel("<html><center>Default Admin: ID=1000, Password=admin123<br/>HR Manager: Mr. Ziad</center></html>");
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        formPanel.add(infoLabel, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Allow Enter key to login
        getRootPane().setDefaultButton(loginButton);
    }
    
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int employeeId = Integer.parseInt(idField.getText());
                String password = new String(passwordField.getPassword());
                
                Employee employee = dataManager.authenticateEmployee(employeeId, password);
                
                if (employee != null) {
                    dispose();
                    if (employee.getEmployeeId() == 1000 || 
                        employee.getJobRole().toLowerCase().contains("manager") ||
                        employee.getJobRole().toLowerCase().contains("hr")) {
                        // Admin/HR Manager
                        new AdminDashboard(dataManager, employee).setVisible(true);
                    } else {
                        // Regular Employee
                        new EmployeeDashboard(dataManager, employee).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                        "Invalid Employee ID or Password!", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Please enter a valid Employee ID!", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


