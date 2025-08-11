import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGUI {
    private JFrame frame;
    private UserManager userManager;
    private PetManager petManager;
    private ShelterManager shelterManager;

    public MainGUI (UserManager userManager, PetManager petManager, ShelterManager shelterManager) {
        this.userManager = userManager;
        this.petManager = petManager;
        this.shelterManager = shelterManager;
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 35, 20));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 0));
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 0));

        JButton registerButton = new JButton("SIGNUP");
        JButton loginButton = new JButton("LOGIN");
        JButton registerShelterButton = new JButton("REGISTER SHELTER");

        styleButtons(registerButton, loginButton, registerShelterButton);

        topRow.add(registerButton);
        topRow.add(loginButton);
        bottomRow.add(registerShelterButton);

        topRow.setMaximumSize(topRow.getPreferredSize());
        bottomRow.setMaximumSize(bottomRow.getPreferredSize());

        buttonPanel.add(topRow);
        buttonPanel.add(Box.createVerticalStrut(6));
        buttonPanel.add(bottomRow);

        this.frame.add(buttonPanel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> openRegistrationForm());
        loginButton.addActionListener(e -> openLoginForm());
        registerShelterButton.addActionListener(e -> openShelterRegistrationForm());

        this.frame.setVisible(true);
    }

    private void openRegistrationForm() {
        String[] roles = {"PETLINK USER", "SHELTER STAFF"};
        String role = (String) JOptionPane.showInputDialog(this.frame, "SELECT ROLE: ", "REGISTER ", JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);
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

        int option = JOptionPane.showConfirmDialog(this.frame, message, "REGISTER " + role, JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role.equals("SHELTER STAFF") && (shelterIdField.getText().isEmpty() || shelterManager.findByJoinCode(shelterIdField.getText()) == null)) {
                JOptionPane.showMessageDialog(this.frame, "MUST ENTER ALL REGISTRATION FIELDS!");
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
                JOptionPane.showMessageDialog(this.frame, role + " REGISTERED SUCCESSFULLY!");
            }

            else if ("PETLINK USER".equals(role)) {
                JOptionPane.showMessageDialog(this.frame, "EMAIL IS REGISTERED WITH EXISTING USER.");
            }

            else {
                JOptionPane.showMessageDialog(this.frame, "SHELTER ID DOES NOT MATCH ANY EXISTING SHELTERS.");
            }
        }
    }

    private void openShelterRegistrationForm() {
        JTextField nameField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField stateField = new JTextField(30);

        Object[] message = {
            "SHELTER NAME: ", nameField,
            "ADDRESS: ", addressField,
            "STATE: ", stateField
        };

        int option = JOptionPane.showConfirmDialog(this.frame, message, "REGISTER SHELTER", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String address = addressField.getText();
            String state = stateField.getText();

            if (name.isEmpty() || address.isEmpty() || state.isEmpty()) {
                JOptionPane.showMessageDialog(this.frame, "MUST ENTER ALL REGISTRATION FIELDS!");
                return;
            }

            Shelter newShelter = new Shelter(name, address, state);
            shelterManager.addShelter(newShelter);
            JOptionPane.showMessageDialog(this.frame, "SHELTER JOIN CODE: " + newShelter.getJoinCode());
            System.out.println(newShelter.getJoinCode());
        }
    }

    // Helper method to style buttons throughout GUIs 
    public static void styleButtons(JButton... buttons) {
        ImageIcon icon = new ImageIcon("icons/paw2.png");
        Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon pawIcon = new ImageIcon(scaled);  
        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);

        for (JButton btn : buttons) {
            btn.setFont(btnFont);
            btn.setIcon(pawIcon);
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            btn.setIconTextGap(8);
            btn.setPreferredSize(new Dimension(140, 40));
            btn.setFocusPainted(false);
        }
    }

    private void openLoginForm() {
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
            "EMAIL: ", emailField,
            "PASSWORD: ", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this.frame, message, "LOGIN", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            User login = userManager.login(email, password);
            if (login != null ) {
                JOptionPane.showMessageDialog(this.frame, "LOGIN SUCCESSFUL! WELCOME " + login.getName().toUpperCase());
                UserGUI userDashboard = new UserGUI(login, userManager, petManager, shelterManager);
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