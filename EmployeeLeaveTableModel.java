import javax.swing.table.DefaultTableModel;

public class EmployeeLeaveTableModel extends DefaultTableModel {
    private static final String[] COLUMNS = {"Leave ID", "Start Date", "End Date", "Days", "Reason", "Status"};
    
    public EmployeeLeaveTableModel() {
        super(COLUMNS, 0);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}


