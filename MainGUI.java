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

        // Holds frame's main text
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("PET-LINK: MAIN MENU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(new Color(90, 122, 173));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("WHERE WE BELIEVE ANYTHING IS PAW-SIBLE");
        subLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subLabel.setForeground(new Color(128, 162, 217));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 13, 0));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subLabel);

        frame.add(titlePanel, BorderLayout.NORTH);

        ImageIcon icon = new ImageIcon("icons/paw2.png");
        Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon pawIcon = new ImageIcon(scaled);        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 35, 20));

        JButton registerButton = new JButton("SIGNUP");
        JButton loginButton = new JButton("LOGIN");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 18);
        registerButton.setFont(btnFont);
        loginButton.setFont(btnFont);

        registerButton.setIcon(pawIcon);
        loginButton.setIcon(pawIcon);

        registerButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        loginButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        
        registerButton.setIconTextGap(8);
        loginButton.setIconTextGap(8);

        registerButton.setPreferredSize(new Dimension(140, 40));
        loginButton.setPreferredSize(new Dimension(140, 40));
        registerButton.setFocusPainted(false);
        loginButton.setFocusPainted(false);

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
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

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this.frame, "MUST ENTER ALL REGISTRATION FIELDS!");
                return;
            }

            User newUser;

            if ("Foster User".equals(role)) {
                newUser = new FosterUser(email, password, name);
            }

            else  {
                // Change to FosterStaff constructor when class is made
                newUser = new FosterUser(email, password, name);
            }

            boolean registered = userManager.addUser(newUser);

            if (registered) {
                JOptionPane.showMessageDialog(this.frame, role + " REGISTERED SUCCESSFULLY!");
            }

            else {
                JOptionPane.showMessageDialog(this.frame, "EMAIL IS REGISTERED WITH EXISTING USER.");
            }
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
                JOptionPane.showMessageDialog(this.frame, "LOGIN SUCCESSFUL! WELCOME " + login.getName().toUpperCase());
                UserGUI userDashboard = new UserGUI(login, userManager, new PetManager());
                frame.setContentPane(userDashboard);
                frame.revalidate();
                frame.repaint();
            } 
            else {
                userManager.printAllUsers();
                JOptionPane.showMessageDialog(this.frame, "INVALID EMAIL OR PASSWORD.");
            }
        }


    }

}