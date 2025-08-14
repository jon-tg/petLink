import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGUI {
    private JFrame frame;
    private UserManager userManager;
    private PetManager petManager;
    private ShelterManager shelterManager;
    private ApplicationManager applicationManager;

    public MainGUI(UserManager userManager, PetManager petManager, ShelterManager shelterManager, ApplicationManager applicationManager) {
        this.userManager = userManager;
        this.petManager = petManager;
        this.shelterManager = shelterManager;
        this.applicationManager = applicationManager;
        SwingUtilities.invokeLater(this:: initialize);
    }

    private void initialize() {
        this.frame = new JFrame("PetLink - Main Menu");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(700, 470);
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

        registerButton.addActionListener(e -> UserRegistrationGUI.showUserRegistrationDialog(this.frame, userManager, shelterManager));
        loginButton.addActionListener(e -> LoginGUI.showLoginDialog(frame, userManager, petManager, shelterManager, applicationManager));
        registerShelterButton.addActionListener(e -> ShelterRegistrationGUI.showShelterRegistrationDialog(frame, shelterManager));

        this.frame.setVisible(true);
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
}