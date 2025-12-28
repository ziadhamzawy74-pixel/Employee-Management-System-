import javax.swing.table.DefaultTableModel;

public class EmployeePayrollTableModel extends DefaultTableModel {
    private static final String[] COLUMNS = {"Payroll ID", "Period", "Working Hours", "Gross Salary", "Tax", "Net Salary"};
    
    public EmployeePayrollTableModel() {
        super(COLUMNS, 0);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}


