import javax.swing.*;

import java.awt.*;
import java.util.List;

public class StaffGUI extends JPanel {
    private final ShelterStaff currentUser;
    private final UserManager userManager;
    private final PetManager petManager;
    private final ShelterManager shelterManager;
    private final ApplicationManager applicationManager;

    public StaffGUI(ShelterStaff staffUser, UserManager um, PetManager pm, ShelterManager sm, ApplicationManager am) {
        this.currentUser = staffUser;
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

        JLabel title = new JLabel(("STAFF PORTAL — " + currentUser.getName()).toUpperCase(), SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        int shelterId = currentUser.getParentShelterID();
        JLabel sub = new JLabel(("SHELTER: " + shelterManager.findById(shelterId).getName() + " • " + currentUser.getEmail()).toUpperCase());
        sub.setForeground(new Color(110, 100, 110));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(sub);
        return panel;
    }

    @SuppressWarnings("unused")
    private JComponent buildMenu() {
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addPetBtn = new JButton("ADD PET");
        JButton viewPetsBtn = new JButton("VIEW PETS");
        JButton viewAppsBtn = new JButton("VIEW APPLICATIONS");
        JButton changeLoginBtn = new JButton("CHANGE LOGIN");
        JButton logoutBtn = new JButton("LOGOUT");

        MainGUI.styleButtons(addPetBtn, viewPetsBtn, viewAppsBtn, changeLoginBtn, logoutBtn);

        JButton[] buttons = {addPetBtn, viewPetsBtn, viewAppsBtn, changeLoginBtn, logoutBtn};
        for (JButton b : buttons) {
            b.setPreferredSize(new Dimension(180, 40));
            center.add(b);
        }

        addPetBtn.addActionListener(e -> openAddPetForm());
        viewPetsBtn.addActionListener(e -> viewPets());
        viewAppsBtn.addActionListener(e -> viewApplications());
        changeLoginBtn.addActionListener(e -> openChangeLoginForm());
        logoutBtn.addActionListener(e -> logout());

        return center;
    }

    private void openChangeLoginForm() {
        ChangeLoginGUI changeLoginGUI = new ChangeLoginGUI(this, userManager,currentUser);
        changeLoginGUI.showDialog();
    }

    private void openAddPetForm() {
        AddPetGUI addPetGUI = new AddPetGUI(petManager, currentUser, this);
        addPetGUI.displayAddPet();
    }

    @SuppressWarnings("unused")
    private void viewPets() {
        ViewPetsGUI viewPetsGUI = new ViewPetsGUI(this, currentUser, petManager, shelterManager, applicationManager, this);
        viewPetsGUI.showDialog();
    }

    @SuppressWarnings("unused")
    private void viewApplications() {
        ViewApplicationsGUI_Staff viewApplicationsGUI = new ViewApplicationsGUI_Staff(currentUser, userManager, petManager, applicationManager, this);

        viewApplicationsGUI.showApplicationsGUI();
    }

    
    private void logout() {
        Window w = SwingUtilities.getWindowAncestor(StaffGUI.this);
        if (w != null) w.dispose();
        SwingUtilities.invokeLater(() -> new MainGUI(userManager, petManager, shelterManager, applicationManager));
    }
}
