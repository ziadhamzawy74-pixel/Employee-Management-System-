import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class DataManager {
    private static final String EMPLOYEES_FILE = "employees.dat";
    private static final String ATTENDANCE_FILE = "attendance.dat";
    private static final String LEAVES_FILE = "leaves.dat";
    private static final String PAYROLL_FILE = "payroll.dat";
    
    private List<Employee> employees;
    private List<Attendance> attendanceRecords;
    private List<Leave> leaves;
    private List<Payroll> payrolls;
    
    public DataManager() {
        employees = new ArrayList<>();
        attendanceRecords = new ArrayList<>();
        leaves = new ArrayList<>();
        payrolls = new ArrayList<>();
        loadData();
        
        // Create default admin account (Mr. Ziad)
        if (employees.isEmpty()) {
            Employee admin = new Employee(1000, "Ziad", "Manager", "ziad.manager@kayan.com", 
                                         "01000000000", Department.HR, "HR Manager", 
                                         50.0, LocalDate.now(), "admin123");
            employees.add(admin);
            saveEmployees();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            // Load employees
            if (new File(EMPLOYEES_FILE).exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EMPLOYEES_FILE));
                employees = (List<Employee>) ois.readObject();
                ois.close();
            }
            
            // Load attendance
            if (new File(ATTENDANCE_FILE).exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ATTENDANCE_FILE));
                attendanceRecords = (List<Attendance>) ois.readObject();
                ois.close();
            }
            
            // Load leaves
            if (new File(LEAVES_FILE).exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LEAVES_FILE));
                leaves = (List<Leave>) ois.readObject();
                ois.close();
            }
            
            // Load payrolls
            if (new File(PAYROLL_FILE).exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PAYROLL_FILE));
                payrolls = (List<Payroll>) ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
    
    private void saveEmployees() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMPLOYEES_FILE));
            oos.writeObject(employees);
            oos.close();
        } catch (Exception e) {
            System.err.println("Error saving employees: " + e.getMessage());
        }
    }
    
    private void saveAttendance() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ATTENDANCE_FILE));
            oos.writeObject(attendanceRecords);
            oos.close();
        } catch (Exception e) {
            System.err.println("Error saving attendance: " + e.getMessage());
        }
    }
    
    private void saveLeaves() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LEAVES_FILE));
            oos.writeObject(leaves);
            oos.close();
        } catch (Exception e) {
            System.err.println("Error saving leaves: " + e.getMessage());
        }
    }
    
    private void savePayrolls() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PAYROLL_FILE));
            oos.writeObject(payrolls);
            oos.close();
        } catch (Exception e) {
            System.err.println("Error saving payrolls: " + e.getMessage());
        }
    }
    
    // Employee methods
    public void addEmployee(Employee employee) {
        employees.add(employee);
        saveEmployees();
    }
    
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }
    
    public Employee getEmployeeById(int id) {
        return employees.stream()
                       .filter(e -> e.getEmployeeId() == id)
                       .findFirst()
                       .orElse(null);
    }
    
    public Employee authenticateEmployee(int id, String password) {
        return employees.stream()
                       .filter(e -> e.getEmployeeId() == id && e.getPassword().equals(password))
                       .findFirst()
                       .orElse(null);
    }
    
    public void updateEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId() == employee.getEmployeeId()) {
                employees.set(i, employee);
                saveEmployees();
                return;
            }
        }
    }
    
    public void deleteEmployee(int id) {
        employees.removeIf(e -> e.getEmployeeId() == id);
        attendanceRecords.removeIf(a -> a.getEmployeeId() == id);
        leaves.removeIf(l -> l.getEmployeeId() == id);
        payrolls.removeIf(p -> p.getEmployeeId() == id);
        saveEmployees();
        saveAttendance();
        saveLeaves();
        savePayrolls();
    }
    
    public int getNextEmployeeId() {
        return employees.stream()
                       .mapToInt(Employee::getEmployeeId)
                       .max()
                       .orElse(1000) + 1;
    }
    
    // Attendance methods
    public void addAttendance(Attendance attendance) {
        attendanceRecords.add(attendance);
        saveAttendance();
    }
    
    public Attendance getTodayAttendance(int employeeId) {
        LocalDate today = LocalDate.now();
        return attendanceRecords.stream()
                               .filter(a -> a.getEmployeeId() == employeeId && 
                                           a.getDate().equals(today))
                               .findFirst()
                               .orElse(null);
    }
    
    public List<Attendance> getEmployeeAttendance(int employeeId) {
        return attendanceRecords.stream()
                               .filter(a -> a.getEmployeeId() == employeeId)
                               .sorted((a1, a2) -> a2.getDate().compareTo(a1.getDate()))
                               .collect(java.util.stream.Collectors.toList());
    }
    
    public List<Attendance> getAllAttendance() {
        return new ArrayList<>(attendanceRecords);
    }
    
    public int getNextAttendanceId() {
        return attendanceRecords.stream()
                               .mapToInt(Attendance::getAttendanceId)
                               .max()
                               .orElse(0) + 1;
    }
    
    public double getTotalHoursForPeriod(int employeeId, YearMonth period) {
        return attendanceRecords.stream()
                               .filter(a -> a.getEmployeeId() == employeeId &&
                                           YearMonth.from(a.getDate()).equals(period))
                               .mapToDouble(Attendance::getWorkingHours)
                               .sum();
    }
    
    // Leave methods
    public void addLeave(Leave leave) {
        leaves.add(leave);
        saveLeaves();
    }
    
    public List<Leave> getEmployeeLeaves(int employeeId) {
        return leaves.stream()
                    .filter(l -> l.getEmployeeId() == employeeId)
                    .sorted((l1, l2) -> l2.getStartDate().compareTo(l1.getStartDate()))
                    .collect(java.util.stream.Collectors.toList());
    }
    
    public List<Leave> getPendingLeaves() {
        return leaves.stream()
                    .filter(l -> l.getStatus().name().equals("PENDING"))
                    .collect(java.util.stream.Collectors.toList());
    }
    
    public List<Leave> getAllLeaves() {
        return new ArrayList<>(leaves);
    }
    
    public Leave getLeaveById(int leaveId) {
        return leaves.stream()
                    .filter(l -> l.getLeaveId() == leaveId)
                    .findFirst()
                    .orElse(null);
    }
    
    public void updateLeave(Leave leave) {
        for (int i = 0; i < leaves.size(); i++) {
            if (leaves.get(i).getLeaveId() == leave.getLeaveId()) {
                leaves.set(i, leave);
                saveLeaves();
                return;
            }
        }
    }
    
    public int getNextLeaveId() {
        return leaves.stream()
                    .mapToInt(Leave::getLeaveId)
                    .max()
                    .orElse(0) + 1;
    }
    
    public int getApprovedLeaveDaysForPeriod(int employeeId, YearMonth period) {
        return leaves.stream()
                    .filter(l -> l.getEmployeeId() == employeeId &&
                               l.getStatus().name().equals("APPROVED") &&
                               YearMonth.from(l.getStartDate()).equals(period))
                    .mapToInt(Leave::getLeaveDays)
                    .sum();
    }
    
    // Payroll methods
    public void addPayroll(Payroll payroll) {
        payrolls.add(payroll);
        savePayrolls();
    }
    
    public List<Payroll> getEmployeePayrolls(int employeeId) {
        return payrolls.stream()
                      .filter(p -> p.getEmployeeId() == employeeId)
                      .sorted((p1, p2) -> p2.getPeriod().compareTo(p1.getPeriod()))
                      .collect(java.util.stream.Collectors.toList());
    }
    
    public List<Payroll> getAllPayrolls() {
        return new ArrayList<>(payrolls);
    }
    
    public Payroll getPayrollForPeriod(int employeeId, YearMonth period) {
        return payrolls.stream()
                      .filter(p -> p.getEmployeeId() == employeeId && 
                                  p.getPeriod().equals(period))
                      .findFirst()
                      .orElse(null);
    }
    
    public int getNextPayrollId() {
        return payrolls.stream()
                      .mapToInt(p -> {
                          try {
                              return p.getPayrollId();
                          } catch (Exception e) {
                              return 0;
                          }
                      })
                      .max()
                      .orElse(0) + 1;
    }
}

