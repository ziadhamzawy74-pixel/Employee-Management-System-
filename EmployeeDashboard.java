import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class EmployeeDashboard extends JFrame {
    private DataManager dataManager;
    private Employee currentUser;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;
    
    public EmployeeDashboard(DataManager dataManager, Employee currentUser) {
        this.dataManager = dataManager;
        this.currentUser = currentUser;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Employee Dashboard - " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
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
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(70, 130, 180));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel welcomeLabel = new JLabel("Kayan - Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.add(welcomeLabel, BorderLayout.WEST);
        
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        welcomePanel.add(statusLabel, BorderLayout.EAST);
        
        add(welcomePanel, BorderLayout.NORTH);
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("My Profile", createProfilePanel());
        tabbedPane.addTab("Attendance", createAttendancePanel());
        tabbedPane.addTab("Leave Requests", createLeavePanel());
        tabbedPane.addTab("My Payroll", createPayrollPanel());
        add(tabbedPane, BorderLayout.CENTER);
        
        updateAttendanceStatus();
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        addLabelAndField(panel, gbc, "Employee ID:", String.valueOf(currentUser.getEmployeeId()), row++);
        addLabelAndField(panel, gbc, "First Name:", currentUser.getFirstName(), row++);
        addLabelAndField(panel, gbc, "Last Name:", currentUser.getLastName(), row++);
        addLabelAndField(panel, gbc, "Email:", currentUser.getEmail(), row++);
        addLabelAndField(panel, gbc, "Phone:", currentUser.getPhoneNumber(), row++);
        addLabelAndField(panel, gbc, "Department:", currentUser.getDepartment().getDisplayName(), row++);
        addLabelAndField(panel, gbc, "Job Role:", currentUser.getJobRole(), row++);
        addLabelAndField(panel, gbc, "Hourly Rate:", String.format("$%.2f", currentUser.getHourlyRate()), row++);
        addLabelAndField(panel, gbc, "Hire Date:", currentUser.getHireDate().toString(), row++);
        
        return panel;
    }
    
    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField field = new JTextField(value);
        field.setEditable(false);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(field, gbc);
    }
    
    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton checkInButton = new JButton("Check In");
        JButton checkOutButton = new JButton("Check Out");
        JButton refreshButton = new JButton("Refresh");
        
        checkInButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkInButton.setPreferredSize(new Dimension(150, 40));
        checkOutButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkOutButton.setPreferredSize(new Dimension(150, 40));
        
        checkInButton.addActionListener(e -> checkIn());
        checkOutButton.addActionListener(e -> checkOut());
        refreshButton.addActionListener(e -> {
            refreshAttendanceTable();
            updateAttendanceStatus();
        });
        
        controlPanel.add(checkInButton);
        controlPanel.add(checkOutButton);
        controlPanel.add(refreshButton);
        panel.add(controlPanel, BorderLayout.NORTH);
        
        // Attendance table
        EmployeeAttendanceTableModel model = new EmployeeAttendanceTableModel();
        JTable table = new JTable(model);
        refreshAttendanceTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void checkIn() {
        LocalDate today = LocalDate.now();
        Attendance todayAttendance = dataManager.getTodayAttendance(currentUser.getEmployeeId());
        
        if (todayAttendance != null && todayAttendance.isCheckedIn()) {
            JOptionPane.showMessageDialog(this, 
                "You have already checked in today!", 
                "Already Checked In", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (todayAttendance != null && todayAttendance.getCheckOutTime() != null) {
            JOptionPane.showMessageDialog(this, 
                "You have already completed today's attendance!", 
                "Already Completed", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (todayAttendance == null) {
            Attendance attendance = new Attendance(
                dataManager.getNextAttendanceId(),
                currentUser.getEmployeeId(),
                today
            );
            dataManager.addAttendance(attendance);
            JOptionPane.showMessageDialog(this, 
                "Check-in successful!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        refreshAttendanceTable();
        updateAttendanceStatus();
    }
    
    private void checkOut() {
        LocalDate today = LocalDate.now();
        Attendance todayAttendance = dataManager.getTodayAttendance(currentUser.getEmployeeId());
        
        if (todayAttendance == null || !todayAttendance.isCheckedIn()) {
            JOptionPane.showMessageDialog(this, 
                "You need to check in first!", 
                "Not Checked In", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (todayAttendance.getCheckOutTime() != null) {
            JOptionPane.showMessageDialog(this, 
                "You have already checked out today!", 
                "Already Checked Out", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        todayAttendance.checkOut();
        dataManager.addAttendance(todayAttendance); // Update existing record
        JOptionPane.showMessageDialog(this, 
            String.format("Check-out successful!\nWorking hours today: %.2f hours", 
                         todayAttendance.getWorkingHours()), 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        refreshAttendanceTable();
        updateAttendanceStatus();
    }
    
    private void updateAttendanceStatus() {
        LocalDate today = LocalDate.now();
        Attendance todayAttendance = dataManager.getTodayAttendance(currentUser.getEmployeeId());
        
        if (todayAttendance == null) {
            statusLabel.setText("Status: Not checked in today");
        } else if (todayAttendance.isCheckedIn()) {
            statusLabel.setText("Status: Checked in - " + todayAttendance.getCheckInTime().toLocalTime().toString());
        } else if (todayAttendance.getCheckOutTime() != null) {
            statusLabel.setText("Status: Completed - " + String.format("%.2f hours", todayAttendance.getWorkingHours()));
        }
    }
    
    private void refreshAttendanceTable() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(1)).getComponent(1)).getViewport().getView();
        EmployeeAttendanceTableModel model = (EmployeeAttendanceTableModel) table.getModel();
        refreshAttendanceTable(model);
    }
    
    private void refreshAttendanceTable(EmployeeAttendanceTableModel model) {
        model.setRowCount(0);
        List<Attendance> attendances = dataManager.getEmployeeAttendance(currentUser.getEmployeeId());
        for (Attendance att : attendances) {
            Object[] row = {
                att.getDate(),
                att.getCheckInTime() != null ? att.getCheckInTime().toLocalTime().toString() : "N/A",
                att.getCheckOutTime() != null ? att.getCheckOutTime().toLocalTime().toString() : "N/A",
                String.format("%.2f", att.getWorkingHours())
            };
            model.addRow(row);
        }
    }
    
    private JPanel createLeavePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton requestLeaveButton = new JButton("Request Leave");
        JButton refreshButton = new JButton("Refresh");
        
        requestLeaveButton.addActionListener(e -> showLeaveRequestDialog());
        refreshButton.addActionListener(e -> refreshLeaveTable());
        
        controlPanel.add(requestLeaveButton);
        controlPanel.add(refreshButton);
        panel.add(controlPanel, BorderLayout.NORTH);
        
        // Leave table
        EmployeeLeaveTableModel model = new EmployeeLeaveTableModel();
        JTable table = new JTable(model);
        refreshLeaveTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showLeaveRequestDialog() {
        JDialog dialog = new JDialog(this, "Request Leave", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Start date
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField startDateField = new JTextField(15);
        panel.add(startDateField, gbc);
        
        // End date
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField endDateField = new JTextField(15);
        panel.add(endDateField, gbc);
        
        // Reason
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Reason:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        JTextArea reasonArea = new JTextArea(5, 15);
        reasonArea.setLineWrap(true);
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        panel.add(reasonScroll, gbc);
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        
        submitButton.addActionListener(e -> {
            try {
                LocalDate startDate = LocalDate.parse(startDateField.getText());
                LocalDate endDate = LocalDate.parse(endDateField.getText());
                String reason = reasonArea.getText().trim();
                
                if (reason.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a reason for leave.");
                    return;
                }
                
                if (endDate.isBefore(startDate)) {
                    JOptionPane.showMessageDialog(dialog, "End date must be after start date.");
                    return;
                }
                
                Leave leave = new Leave(
                    dataManager.getNextLeaveId(),
                    currentUser.getEmployeeId(),
                    startDate,
                    endDate,
                    reason
                );
                
                dataManager.addLeave(leave);
                JOptionPane.showMessageDialog(dialog, "Leave request submitted successfully!");
                dialog.dispose();
                refreshLeaveTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void refreshLeaveTable() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(2)).getComponent(1)).getViewport().getView();
        EmployeeLeaveTableModel model = (EmployeeLeaveTableModel) table.getModel();
        refreshLeaveTable(model);
    }
    
    private void refreshLeaveTable(EmployeeLeaveTableModel model) {
        model.setRowCount(0);
        List<Leave> leaves = dataManager.getEmployeeLeaves(currentUser.getEmployeeId());
        for (Leave leave : leaves) {
            Object[] row = {
                leave.getLeaveId(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getLeaveDays(),
                leave.getReason(),
                leave.getStatus().name()
            };
            model.addRow(row);
        }
    }
    
    private JPanel createPayrollPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshPayrollTable());
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        EmployeePayrollTableModel model = new EmployeePayrollTableModel();
        JTable table = new JTable(model);
        refreshPayrollTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshPayrollTable() {
        JTable table = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(3)).getComponent(1)).getViewport().getView();
        EmployeePayrollTableModel model = (EmployeePayrollTableModel) table.getModel();
        refreshPayrollTable(model);
    }
    
    private void refreshPayrollTable(EmployeePayrollTableModel model) {
        model.setRowCount(0);
        List<Payroll> payrolls = dataManager.getEmployeePayrolls(currentUser.getEmployeeId());
        for (Payroll payroll : payrolls) {
            Object[] row = {
                payroll.getPayrollId(),
                payroll.getPeriod().toString(),
                String.format("%.2f", payroll.getTotalWorkingHours()),
                String.format("$%.2f", payroll.getGrossSalary()),
                String.format("$%.2f", payroll.getTaxDeduction()),
                String.format("$%.2f", payroll.getNetSalary())
            };
            model.addRow(row);
        }
    }
}

