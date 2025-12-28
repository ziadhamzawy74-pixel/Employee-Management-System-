import javax.swing.table.DefaultTableModel;

public class EmployeeAttendanceTableModel extends DefaultTableModel {
    private static final String[] COLUMNS = {"Date", "Check-in Time", "Check-out Time", "Working Hours"};
    
    public EmployeeAttendanceTableModel() {
        super(COLUMNS, 0);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}


