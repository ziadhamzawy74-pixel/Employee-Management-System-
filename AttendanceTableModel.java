import javax.swing.table.DefaultTableModel;

public class AttendanceTableModel extends DefaultTableModel {
    private static final String[] COLUMNS = {"Attendance ID", "Employee ID", "Date", "Check-in", "Check-out", "Working Hours"};
    
    public AttendanceTableModel() {
        super(COLUMNS, 0);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}


