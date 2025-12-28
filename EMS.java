import javax.swing.SwingUtilities;

public class EMS {
    public static void main(String[] args) {
        // Initialize data manager
        DataManager dataManager = new DataManager();
        
        // Launch GUI on EDT
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(dataManager);
            loginFrame.setVisible(true);
        });
    }
}

