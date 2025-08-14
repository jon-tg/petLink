import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UserRegistrationGUI {
    public static void showUserRegistrationDialog(JFrame frame, UserManager userManager, ShelterManager shelterManager) {
        String[] roles = {"PETLINK USER", "SHELTER STAFF"};
        String role = (String) JOptionPane.showInputDialog(frame, "SELECT ROLE: ", "REGISTER ", JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);
        if (role == null) return;

        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField shelterIdField = new JTextField(15);

        Object[] message = {
            "NAME: ", nameField,
            "EMAIL: ", emailField,
            "PASSWORD: ", passwordField
        };

        if (role.equals("SHELTER STAFF")) {
            message = new Object[] {
                "NAME: ", nameField,
                "EMAIL: ", emailField,
                "PASSWORD: ", passwordField,
                "SHELTER ID: ", shelterIdField
            };
        }

        int option = JOptionPane.showConfirmDialog(frame, message, "REGISTER " + role, JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role.equals("SHELTER STAFF") && (shelterIdField.getText().isEmpty() || shelterManager.findByJoinCode(shelterIdField.getText()) == null)) {
                JOptionPane.showMessageDialog(frame, "MUST ENTER ALL REGISTRATION FIELDS!");
                return;
            }

            User newUser;

            if ("PETLINK USER".equals(role)) {
                newUser = new FosterUser(email, password, name);
            }

            else  {
                Shelter shelter = shelterManager.findByJoinCode(shelterIdField.getText());
                newUser = new ShelterStaff(email, password, name, shelter.getShelterID());
            }

            boolean registered = userManager.addUser(newUser);

            if (registered) {
                JOptionPane.showMessageDialog(frame, role + " REGISTERED SUCCESSFULLY!");
            }

            else if ("PETLINK USER".equals(role)) {
                JOptionPane.showMessageDialog(frame, "EMAIL IS REGISTERED WITH EXISTING USER.");
            }

            else {
                JOptionPane.showMessageDialog(frame, "SHELTER ID DOES NOT MATCH ANY EXISTING SHELTERS.");
            }
        }
    }
}
