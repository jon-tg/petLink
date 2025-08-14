import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginGUI {
    public static void showLoginDialog(JFrame frame, UserManager userManager, PetManager petManager, ShelterManager shelterManager, ApplicationManager applicationManager) {
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
            "EMAIL: ", emailField,
            "PASSWORD: ", passwordField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "LOGIN", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            User login = userManager.login(email, password);
            if (login != null ) {
                JOptionPane.showMessageDialog(frame, "LOGIN SUCCESSFUL! WELCOME " + login.getName().toUpperCase());
                
                if (login instanceof FosterUser fu) {
                    UserGUI userDashboard = new UserGUI(fu, userManager, petManager, shelterManager, applicationManager);
                    frame.setContentPane(userDashboard);

                } else if (login instanceof ShelterStaff su) {
                    StaffGUI staffDashboard = new StaffGUI(su, userManager, petManager, shelterManager, applicationManager);
                    frame.setContentPane(staffDashboard);
                }

                frame.revalidate();
                frame.repaint();
            } 
            else {
                userManager.printAllUsers();
                JOptionPane.showMessageDialog(frame, "INVALID EMAIL OR PASSWORD.");
            }
        }
    }
}
