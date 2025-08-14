import javax.swing.*;
import java.awt.*;

public class ChangeLoginGUI {
    private final Component parentComponent;
    private final UserManager userManager;
    private final FosterUser currentUser;
    private final Runnable onUpdateCallback;

    public ChangeLoginGUI(Component parentComponent, UserManager userManager, FosterUser currentUser, Runnable onUpdateCallback) {
        this.parentComponent = parentComponent;
        this.userManager = userManager;
        this.currentUser = currentUser;
        this.onUpdateCallback = onUpdateCallback;
    }

    public void showDialog() {
        String[] actions = {"EMAIL", "PASSWORD"};
        String action = (String) JOptionPane.showInputDialog(
            parentComponent, 
            "CHANGE: ", 
            "CHANGE LOGIN", 
            JOptionPane.PLAIN_MESSAGE, 
            null, 
            actions, 
            actions[0]
        );
        
        if (action == null) return;

        if ("EMAIL".equals(action)) {
            showEmailChangeDialog();
        } else {
            showPasswordChangeDialog();
        }
    }

    private void showEmailChangeDialog() {
        JTextField emailField = new JTextField(15);
        emailField.setText(currentUser.getEmail());
        
        Object[] message = {
            "NEW EMAIL:", emailField
        };

        int option = JOptionPane.showConfirmDialog(
            parentComponent,
            message,
            "CHANGE EMAIL",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (option != JOptionPane.OK_OPTION) return;

        String newEmail = emailField.getText().trim();
        if (newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(
                parentComponent, 
                "EMAIL CANNOT BE EMPTY", 
                "ERROR", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (newEmail.equals(currentUser.getEmail())) {
            JOptionPane.showMessageDialog(
                parentComponent,
                "NEW EMAIL MUST BE DIFFERENT FROM CURRENT EMAIL",
                "ERROR",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (userManager.findByEmail(newEmail) != null) {
            JOptionPane.showMessageDialog(
                parentComponent,
                "EMAIL IS ALREADY REGISTERED TO ANOTHER USER",
                "ERROR",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        userManager.changeUserEmail(currentUser.getID(), newEmail);
        JOptionPane.showMessageDialog(parentComponent, "EMAIL UPDATED SUCCESSFULLY");
    }

    private void showPasswordChangeDialog() {
        JPasswordField newPasswordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        
        Object[] message = {
            "NEW PASSWORD:", newPasswordField,
            "CONFIRM PASSWORD:", confirmPasswordField
        };

        int option = JOptionPane.showConfirmDialog(
            parentComponent,
            message,
            "CHANGE PASSWORD",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (option != JOptionPane.OK_OPTION) return;

        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(
                parentComponent, 
                "PASSWORD CANNOT BE EMPTY", 
                "ERROR", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                parentComponent, 
                "PASSWORDS DO NOT MATCH", 
                "ERROR", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        userManager.changeUserPassword(currentUser.getID(), newPassword);
        JOptionPane.showMessageDialog(parentComponent, "PASSWORD UPDATED SUCCESSFULLY");
    }
}