import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;

public class Payroll implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int payrollId;
    private int employeeId;
    private YearMonth period;
    private double totalWorkingHours;
    private double grossSalary;
    private double taxDeduction;
    private double otherDeductions;
    private double netSalary;
    private LocalDate payDate;
    private boolean processed;
    
    private static final double TAX_RATE = 0.20;
    private static final double STANDARD_WORKING_DAYS = 22;
    
    public Payroll(int payrollId, int employeeId, YearMonth period) {
        this.payrollId = payrollId;
        this.employeeId = employeeId;
        this.period = period;
        this.totalWorkingHours = 0.0;
        this.processed = false;
        this.payDate = LocalDate.now();
    }
    
    public void calculatePayroll(double totalHours, double hourlyRate, int leaveDays) {
        this.totalWorkingHours = totalHours;
        
        double expectedHours = STANDARD_WORKING_DAYS * 8;
        double adjustedHours = Math.max(0, expectedHours - (leaveDays * 8));
        
        if (totalHours > 0) {
            this.grossSalary = totalHours * hourlyRate;
        } else {
            this.grossSalary = adjustedHours * hourlyRate;
        }
        
        this.taxDeduction = grossSalary * TAX_RATE;
        this.otherDeductions = 0.0;
        this.netSalary = grossSalary - taxDeduction - otherDeductions;
        this.processed = true;
    }
    
    public int getPayrollId() {
        return payrollId;
    }
    
    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public YearMonth getPeriod() {
        return period;
    }
    
    public void setPeriod(YearMonth period) {
        this.period = period;
    }
    
    public double getTotalWorkingHours() {
        return totalWorkingHours;
    }
    
    public void setTotalWorkingHours(double totalWorkingHours) {
        this.totalWorkingHours = totalWorkingHours;
    }
    
    public double getGrossSalary() {
        return grossSalary;
    }
    
    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }
    
    public double getTaxDeduction() {
        return taxDeduction;
    }
    
    public void setTaxDeduction(double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }
    
    public double getOtherDeductions() {
        return otherDeductions;
    }
    
    public void setOtherDeductions(double otherDeductions) {
        this.otherDeductions = otherDeductions;
    }
    
    public double getNetSalary() {
        return netSalary;
    }
    
    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }
    
    public LocalDate getPayDate() {
        return payDate;
    }
    
    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }
    
    public boolean isProcessed() {
        return processed;
    }
    
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
    
    @Override
    public String toString() {
        return String.format("Period: %s, Hours: %.2f, Gross: $%.2f, Net: $%.2f", 
                           period, totalWorkingHours, grossSalary, netSalary);
    }
}
