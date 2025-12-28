import java.io.Serializable;
import java.time.LocalDate;

public class Leave implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int leaveId;
    private int employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
    private String comments;
    
    public Leave(int leaveId, int employeeId, LocalDate startDate, LocalDate endDate, String reason) {
        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = LeaveStatus.PENDING;
        this.comments = "";
    }
    
    public int getLeaveDays() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    
    public int getLeaveId() {
        return leaveId;
    }
    
    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public LeaveStatus getStatus() {
        return status;
    }
    
    public void setStatus(LeaveStatus status) {
        this.status = status;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    @Override
    public String toString() {
        return String.format("Leave ID: %d, Dates: %s to %s, Status: %s, Reason: %s", 
                           leaveId, startDate, endDate, status, reason);
    }
}
