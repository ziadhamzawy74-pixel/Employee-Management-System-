import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;

public class AdminDashboard extends JFrame {
    private DataManager dataManager;
    private Employee currentUser;
    private JTabbedPane tabbedPane;
    
    public AdminDashboard(DataManager dataManager, Employee currentUser) {
        this.dataManager = dataManager;
        this.currentUser = currentUser;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Admin Dashboard - " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            dispose();
            new LoginFrame(dataManager).setVisible(true);
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        // Welcome panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(70, 130, 180));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Kayan - Welcome, " + currentUser.getFullName() + " (HR Manager)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.NORTH);
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employee Management", createEmployeeManagementPanel());
        tabbedPane.addTab("Attendance Tracking", createAttendancePanel());
        tabbedPane.addTab("Payroll Management", createPayrollPanel());
        tabbedPane.addTab("Leave Management", createLeaveManagementPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createEmployeeManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Employee");
        JButton editButton = new JButton("Edit Employee");
        JButton deleteButton = new JButton("Delete Employee");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(e -> showAddEmployeeDialog());
        editButton.addActionListener(e -> showEditEmployeeDialog());
        deleteButton.addActionListener(e -> deleteSelectedEmployee());
        refreshButton.addActionListener(e -> refreshEmployeeTable());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        // Employee table
        EmployeeTableModel model = new EmployeeTableModel();
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshEmployeeTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshEmployeeTable() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(0)).getComponent(1)).getViewport().getView();
        EmployeeTableModel model = (EmployeeTableModel) table.getModel();
        refreshEmployeeTable(model);
    }
    
    private void refreshEmployeeTable(EmployeeTableModel model) {
        model.setRowCount(0);
        for (Employee emp : dataManager.getAllEmployees()) {
            Object[] row = {
                emp.getEmployeeId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getEmail(),
                emp.getPhoneNumber(),
                emp.getDepartment().getDisplayName(),
                emp.getJobRole(),
                String.format("%.2f", emp.getHourlyRate())
            };
            model.addRow(row);
        }
    }
    
    private void showAddEmployeeDialog() {
        EmployeeDialog dialog = new EmployeeDialog(this, dataManager, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshEmployeeTable();
        }
    }
    
    private void showEditEmployeeDialog() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(0)).getComponent(1)).getViewport().getView();
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit.");
            return;
        }
        
        int employeeId = (Integer) table.getValueAt(selectedRow, 0);
        Employee employee = dataManager.getEmployeeById(employeeId);
        
        EmployeeDialog dialog = new EmployeeDialog(this, dataManager, employee);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshEmployeeTable();
        }
    }
    
    private void deleteSelectedEmployee() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(0)).getComponent(1)).getViewport().getView();
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            return;
        }
        
        int employeeId = (Integer) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this employee?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dataManager.deleteEmployee(employeeId);
            refreshEmployeeTable();
            JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
        }
    }
    
    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshAttendanceTable());
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        AttendanceTableModel model = new AttendanceTableModel();
        JTable table = new JTable(model);
        refreshAttendanceTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshAttendanceTable() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(1)).getComponent(1)).getViewport().getView();
        AttendanceTableModel model = (AttendanceTableModel) table.getModel();
        refreshAttendanceTable(model);
    }
    
    private void refreshAttendanceTable(AttendanceTableModel model) {
        model.setRowCount(0);
        for (Attendance att : dataManager.getAllAttendance()) {
            Object[] row = {
                att.getAttendanceId(),
                att.getEmployeeId(),
                att.getDate(),
                att.getCheckInTime() != null ? att.getCheckInTime().toLocalTime().toString() : "N/A",
                att.getCheckOutTime() != null ? att.getCheckOutTime().toLocalTime().toString() : "N/A",
                String.format("%.2f", att.getWorkingHours())
            };
            model.addRow(row);
        }
    }
    
    private JPanel createPayrollPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        
        JLabel employeeLabel = new JLabel("Employee ID:");
        JTextField employeeIdField = new JTextField(10);
        
        JLabel periodLabel = new JLabel("Period (YYYY-MM):");
        JTextField periodField = new JTextField(10);
        periodField.setText(YearMonth.now().toString());
        
        JButton calculateButton = new JButton("Calculate Payroll");
        JButton refreshButton = new JButton("Refresh");
        
        calculateButton.addActionListener(e -> {
            try {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                YearMonth period = YearMonth.parse(periodField.getText());
                
                Employee emp = dataManager.getEmployeeById(employeeId);
                if (emp == null) {
                    JOptionPane.showMessageDialog(this, "Employee not found!");
                    return;
                }
                
                double totalHours = dataManager.getTotalHoursForPeriod(employeeId, period);
                int leaveDays = dataManager.getApprovedLeaveDaysForPeriod(employeeId, period);
                
                Payroll payroll = dataManager.getPayrollForPeriod(employeeId, period);
                if (payroll == null) {
                    payroll = new Payroll(dataManager.getNextPayrollId(), employeeId, period);
                }
                
                payroll.calculatePayroll(totalHours, emp.getHourlyRate(), leaveDays);
                dataManager.addPayroll(payroll);
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Payroll calculated successfully!\nNet Salary: $%.2f", 
                                 payroll.getNetSalary()));
                refreshPayrollTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        refreshButton.addActionListener(e -> refreshPayrollTable());
        
        controlPanel.add(employeeLabel);
        controlPanel.add(employeeIdField);
        controlPanel.add(periodLabel);
        controlPanel.add(periodField);
        controlPanel.add(calculateButton);
        controlPanel.add(refreshButton);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        PayrollTableModel model = new PayrollTableModel();
        JTable table = new JTable(model);
        refreshPayrollTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshPayrollTable() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(2)).getComponent(1)).getViewport().getView();
        PayrollTableModel model = (PayrollTableModel) table.getModel();
        refreshPayrollTable(model);
    }
    
    private void refreshPayrollTable(PayrollTableModel model) {
        model.setRowCount(0);
        for (Payroll payroll : dataManager.getAllPayrolls()) {
            Object[] row = {
                payroll.getPayrollId(),
                payroll.getEmployeeId(),
                payroll.getPeriod().toString(),
                String.format("%.2f", payroll.getTotalWorkingHours()),
                String.format("$%.2f", payroll.getGrossSalary()),
                String.format("$%.2f", payroll.getTaxDeduction()),
                String.format("$%.2f", payroll.getNetSalary())
            };
            model.addRow(row);
        }
    }
    
    private JPanel createLeaveManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshLeaveTable());
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        LeaveTableModel model = new LeaveTableModel();
        JTable table = new JTable(model);
        
        // Add action listener for double-click to approve/reject
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int leaveId = (Integer) model.getValueAt(row, 0);
                        Leave leave = dataManager.getLeaveById(leaveId);
                        if (leave != null && leave.getStatus().name().equals("PENDING")) {
                            showLeaveActionDialog(leave);
                        }
                    }
                }
            }
        });
        
        refreshLeaveTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showLeaveActionDialog(Leave leave) {
        JDialog dialog = new JDialog(this, "Leave Action", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel infoLabel = new JLabel(String.format(
            "<html>Employee ID: %d<br/>Dates: %s to %s<br/>Days: %d<br/>Reason: %s</html>",
            leave.getEmployeeId(), leave.getStartDate(), leave.getEndDate(),
            leave.getLeaveDays(), leave.getReason()));
        panel.add(infoLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");
        JButton cancelButton = new JButton("Cancel");
        
        approveButton.addActionListener(e -> {
            leave.setStatus(LeaveStatus.APPROVED);
            leave.setComments("Approved by " + currentUser.getFullName());
            dataManager.updateLeave(leave);
            dialog.dispose();
            refreshLeaveTable();
            JOptionPane.showMessageDialog(this, "Leave approved successfully.");
        });
        
        rejectButton.addActionListener(e -> {
            leave.setStatus(LeaveStatus.REJECTED);
            leave.setComments("Rejected by " + currentUser.getFullName());
            dataManager.updateLeave(leave);
            dialog.dispose();
            refreshLeaveTable();
            JOptionPane.showMessageDialog(this, "Leave rejected.");
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void refreshLeaveTable() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(3)).getComponent(1)).getViewport().getView();
        LeaveTableModel model = (LeaveTableModel) table.getModel();
        refreshLeaveTable(model);
    }
    
    private void refreshLeaveTable(LeaveTableModel model) {
        model.setRowCount(0);
        for (Leave leave : dataManager.getAllLeaves()) {
            Object[] row = {
                leave.getLeaveId(),
                leave.getEmployeeId(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getLeaveDays(),
                leave.getReason(),
                leave.getStatus().name()
            };
            model.addRow(row);
        }
    }
}

