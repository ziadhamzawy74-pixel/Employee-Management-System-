import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;

public class Attendance implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int attendanceId;
    private int employeeId;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private double workingHours;
    
    public Attendance(int attendanceId, int employeeId, LocalDate date) {
        this.attendanceId = attendanceId;
        this.employeeId = employeeId;
        this.date = date;
        this.checkInTime = LocalDateTime.now();
        this.workingHours = 0.0;
    }
    
    public void checkOut() {
        if (checkOutTime == null && checkInTime != null) {
            this.checkOutTime = LocalDateTime.now();
            calculateWorkingHours();
        }
    }
    
    private void calculateWorkingHours() {
        if (checkInTime != null && checkOutTime != null) {
            Duration duration = Duration.between(checkInTime, checkOutTime);
            this.workingHours = duration.toMinutes() / 60.0;
        }
    }
    
    public int getAttendanceId() {
        return attendanceId;
    }
    
    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }
    
    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
        if (checkOutTime != null) {
            calculateWorkingHours();
        }
    }
    
    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }
    
    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
        calculateWorkingHours();
    }
    
    public double getWorkingHours() {
        return workingHours;
    }
    
    public void setWorkingHours(double workingHours) {
        this.workingHours = workingHours;
    }
    
    public boolean isCheckedIn() {
        return checkInTime != null && checkOutTime == null;
    }
    
    @Override
    public String toString() {
        return String.format("Date: %s, Check-in: %s, Check-out: %s, Hours: %.2f", 
                           date, 
                           checkInTime != null ? checkInTime.toLocalTime().toString() : "N/A",
                           checkOutTime != null ? checkOutTime.toLocalTime().toString() : "N/A",
                           workingHours);
    }
}
