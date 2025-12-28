import javax.swing.table.DefaultTableModel;

public class PayrollTableModel extends DefaultTableModel {
    private static final String[] COLUMNS = {"Payroll ID", "Employee ID", "Period", "Hours", "Gross", "Tax", "Net"};
    
    public PayrollTableModel() {
        super(COLUMNS, 0);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}


