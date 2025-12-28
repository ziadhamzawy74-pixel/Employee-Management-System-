import javax.swing.table.DefaultTableModel;

public class LeaveTableModel extends DefaultTableModel {
    private static final String[] COLUMNS = {"Leave ID", "Employee ID", "Start Date", "End Date", "Days", "Reason", "Status"};
    
    public LeaveTableModel() {
        super(COLUMNS, 0);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}


