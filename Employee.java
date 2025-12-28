import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Department department;
    private String jobRole;
    private double hourlyRate;
    private LocalDate hireDate;
    private String password;
    
    public Employee(int employeeId, String firstName, String lastName, String email, 
                   String phoneNumber, Department department, String jobRole, 
                   double hourlyRate, LocalDate hireDate, String password) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.jobRole = jobRole;
        this.hourlyRate = hourlyRate;
        this.hireDate = hireDate;
        this.password = password;
    }
    
    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
    
    public String getJobRole() {
        return jobRole;
    }
    
    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }
    
    public double getHourlyRate() {
        return hourlyRate;
    }
    
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s %s, Department: %s, Role: %s", 
                           employeeId, firstName, lastName, department.getDisplayName(), jobRole);
    }
}

