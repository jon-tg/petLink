import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGUI {

    private JFrame frame;
    private UserManager userManager;

    public MainGUI (UserManager userManager) {
        this.userManager = userManager;
        SwingUtilities.invokeLater(this:: initialize);
    }

    private void initialize() {
        this.frame = new JFrame("PetLink - Main Menu");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(700, 450);
        this.frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("PETLINK MAIN MENU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        this.frame.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        JButton registerButton = new JButton("REGISTER");
        JButton loginButton = new JButton("LOGIN");

        registerButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        Font btnFont = new Font("Segoe UI", Font.BOLD, 18);
        registerButton.setFont(btnFont);
        loginButton.setFont(btnFont);


        this.frame.add(buttonPanel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> openRegistrationForm());
        loginButton.addActionListener(e -> openLoginForm());

        this.frame.setVisible(true);
    }

    private void openRegistrationForm() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        String[] roles = {"FOSTER USER", "SHELTER STAFF"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);

        Object[] message = {
            "NAME: ", nameField,
            "EMAIL: ", emailField,
            "PASSWORD: ", passwordField,
            "ROLE: ", roleDropdown
        };

        int option = JOptionPane.showConfirmDialog(this.frame, message, "REGISTER", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleDropdown.getSelectedItem();

            User newUser;

            if ("Foster User".equals(role)) {
                newUser = new FosterUser(email, password, name);
            }

            // Change to FosterStaff constructor when class is made
            else  { newUser = new FosterUser(email, password, name); }
    
            userManager.addUser(newUser);
            JOptionPane.showMessageDialog(this.frame, role + " REGISTERED SUCCESSFULLY!");
        }

    }

    private void openLoginForm() {
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
            "EMAIL: : ", emailField,
            "PASSWORD: ", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this.frame, message, "LOGIN", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            User login = userManager.login(email, password);
            if (login != null ){
                JOptionPane.showMessageDialog(this.frame, "LOGIN SUCCESSFUL! WELCOME " + login.getName());
                //  OPEN DASHBOARD FOR ROLE
            } 
            else {
                userManager.printAllUsers();
                JOptionPane.showMessageDialog(this.frame, "INVALID EMAIL OR PASSWORD.");
            }
        }


    }

}