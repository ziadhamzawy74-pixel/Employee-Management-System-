import javax.swing.table.DefaultTableModel;

public class EmployeeTableModel extends DefaultTableModel {
    private static final String[] COLUMNS = {"ID", "First Name", "Last Name", "Email", "Phone", "Department", "Role", "Hourly Rate"};
    
    public EmployeeTableModel() {
        super(COLUMNS, 0);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}


