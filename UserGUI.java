import javax.swing.*;

import java.awt.*;

public class UserGUI extends JPanel {
    private final FosterUser currentUser;
    private final UserManager userManager;
    private final PetManager petManager;
    private final ShelterManager shelterManager;
    private final ApplicationManager applicationManager;

    public UserGUI(FosterUser user, UserManager um, PetManager pm, ShelterManager sm, ApplicationManager am) {
        this.currentUser = user;
        this.userManager = um;
        this.petManager = pm;
        this.shelterManager = sm;
        this.applicationManager = am;

        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildMenu(), BorderLayout.CENTER);
    }

    private JComponent buildHeader() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel title = new JLabel("WELCOME, " + currentUser.getName().toUpperCase(), SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(currentUser.getEmail().toUpperCase());
        sub.setForeground(new Color(110, 100, 110));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(sub);
        return panel;
    }

    @SuppressWarnings("unused")
    private JComponent buildMenu() {
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton browsePets = new JButton("BROWSE PETS");
        JButton viewShelters = new JButton("VIEW SHELTERS");
        JButton viewApplications = new JButton("VIEW APPLICATIONS");
        JButton changeLogin = new JButton("CHANGE LOGIN");
        JButton logout = new JButton("LOGOUT");
        JButton[] buttons = {browsePets, viewShelters, viewApplications, changeLogin, logout};

        MainGUI.styleButtons(browsePets, viewShelters, viewApplications, changeLogin, logout);
        for (JButton btn: buttons) {
            btn.setPreferredSize(new Dimension(180, 40));
            center.add(btn);
        }

        browsePets.addActionListener(e -> viewPets());
        viewShelters.addActionListener(e -> viewShelters());
        viewApplications.addActionListener(e -> viewApplications());
        changeLogin.addActionListener(e -> openChangeLoginForm());
        logout.addActionListener(e -> logout());
        return center;
    }

    private void openChangeLoginForm() {
        ChangeLoginGUI changeLoginGUI = new ChangeLoginGUI(this, userManager, currentUser);
        changeLoginGUI.showDialog();
    }

    private void viewShelters() {
        ViewSheltersGUI viewSheltersGUI = new ViewSheltersGUI(this.shelterManager);
        viewSheltersGUI.showSheltersGUI();
    }

    private void viewPets() {
        ViewPetsGUI viewPetsGUI = new ViewPetsGUI(this, currentUser, petManager, shelterManager, applicationManager);
        viewPetsGUI.showDialog();
    }

    private void viewApplications() {
        ViewApplicationsGUI viewApplicationsGUI = new ViewApplicationsGUI(currentUser, userManager, petManager, applicationManager);
        viewApplicationsGUI.showApplicationsGUI();
    }

    private void logout() {
        Window window = SwingUtilities.getWindowAncestor(UserGUI.this);
        // Closes the window
        if (window != null) {
            window.dispose();
        }
        // Show MainGUI again
        SwingUtilities.invokeLater(() -> new MainGUI(userManager, petManager, shelterManager, applicationManager));
    }
}