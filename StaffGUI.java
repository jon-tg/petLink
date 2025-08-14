import javax.swing.*;
import javax.swing.text.View;

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
        JTextField nameField = new JTextField(15);
        JTextField speciesField = new JTextField(15);
        JTextField breedField = new JTextField(15);
        JTextField ageField = new JTextField(15);
        JTextField temperamentField = new JTextField(15);

        Object[] msg = new Object[]{
                "NAME:", nameField,
                "SPECIES:", speciesField,
                "BREED:", breedField,
                "AGE:", ageField,
                "TEMPERAMENT:", temperamentField,
        };

        int option = JOptionPane.showConfirmDialog(this, msg, "ADD PET", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String name = nameField.getText();
        String species = speciesField.getText();
        String breed = breedField.getText();
        int age = Integer.parseInt(ageField.getText());
        String temperament = temperamentField.getText();

        if (name.isEmpty() || species.isEmpty() || breed.isEmpty() || age < 0 || temperament.isEmpty()) {
            JOptionPane.showMessageDialog(this, "MUST ENTER ALL FIELDS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        petManager.addPet(name, species, breed, age, temperament, currentUser.getParentShelterID());
        JOptionPane.showMessageDialog(this, "PET ADDED");
    }

    @SuppressWarnings("unused")
    private void viewPets() {
        ViewPetsGUI viewPetsGUI = new ViewPetsGUI(this, currentUser, petManager, shelterManager, applicationManager, this);
        viewPetsGUI.showDialog();
    }

    @SuppressWarnings("unused")
    private void viewApplications() {
        JFrame f = new JFrame("APPLICATIONS");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout(10, 8));
        container.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel title = new JLabel("BROWSE APPLICATIONS", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        container.add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JScrollPane scroll = new JScrollPane(cards);
        container.add(scroll, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn   = new JButton("Close");
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        controls.add(refreshBtn);
        controls.add(closeBtn);
        container.add(controls, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadApplicationCards(cards));
        closeBtn.addActionListener(e -> f.dispose());

        loadApplicationCards(cards);

        f.setContentPane(container);
        f.setSize(900, 560);
        f.setVisible(true);
    }

    private void loadApplicationCards(JPanel container) {
        container.removeAll();

        List<FosterApplication> apps = applicationManager.getByStatus(applicationManager.getByShelter(currentUser.getParentShelterID()), "Pending");

        if (apps == null || apps.isEmpty()) {
            JLabel empty = new JLabel("NO APPLICATIONS FOUND");
            empty.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            container.add(empty);
        } else {
            for (FosterApplication app : apps) {
                container.add(makeApplicationCard(app, () -> loadApplicationCards(container)));
            }
        }

        container.revalidate();
        container.repaint();
    }

    @SuppressWarnings("unused")
    private JComponent makeApplicationCard(FosterApplication app, Runnable afterSave) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210,210,210)),
                BorderFactory.createEmptyBorder(10,12,10,12)
        ));
        card.setPreferredSize(new Dimension(300, 200));

        Pet pet = petManager.findById(app.getPetId());
        User applicant = userManager.findById(app.getUserId()); 

        String petName = pet.getName();
        String applicantName = applicant.getName();
        String applicantEmail = applicant.getEmail();

        JLabel heading = new JLabel(("APPLICATION #" + app.getApplicationID()).toUpperCase());
        heading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel petLbl = new JLabel("PET: " + petName);
        petLbl.setForeground(new Color(90,90,90));
        petLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userLbl = new JLabel("APPLICANT: " + applicantName + " • " + applicantEmail);
        userLbl.setForeground(new Color(90,90,90));
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel submittedLbl = new JLabel("SUBMITTED: " + app.getTimestamp().toString());
        submittedLbl.setForeground(new Color(90,90,90));
        submittedLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLbl = new JLabel("STATUS: " + app.getStatus().toUpperCase());
        statusLbl.setForeground(new Color(120,90,90));
        statusLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton approveBtn = new JButton("APPROVE");
        JButton rejectBtn  = new JButton("REJECT");
        actions.add(approveBtn);
        actions.add(rejectBtn);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        boolean isPending = "PENDING".equalsIgnoreCase(app.getStatus());
        approveBtn.setEnabled(isPending);
        rejectBtn.setEnabled(isPending);

        approveBtn.addActionListener(e -> {
            if (confirm("APPROVE APPLICATION?")) {
                applicationManager.updateStatus(app.getApplicationID(), "APPROVED");
                petManager.updateStatus(app.getPetId(), "FOSTERED");
                JOptionPane.showMessageDialog(this, "APPLICATION APPROVED");
                if (afterSave != null) afterSave.run();
            }
        });

        rejectBtn.addActionListener(e -> {
            if (confirm("REJECT APPLICATION?")) {
                applicationManager.updateStatus(app.getApplicationID(), "REJECTED");
                petManager.updateStatus(app.getPetId(), "AVAILABLE");
                JOptionPane.showMessageDialog(this, "APPLICATION REJECTED");
                if (afterSave != null) afterSave.run();
            }
        });

        card.add(heading);
        card.add(Box.createVerticalStrut(2));
        card.add(petLbl);
        card.add(userLbl);
        card.add(submittedLbl);
        card.add(statusLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(actions);

        return card;
    }

    private boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(
                this, msg, "CONFIRM", JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION;
    }

    private void logout() {
        Window w = SwingUtilities.getWindowAncestor(StaffGUI.this);
        if (w != null) w.dispose();
        SwingUtilities.invokeLater(() -> new MainGUI(userManager, petManager, shelterManager, applicationManager));
    }
}
