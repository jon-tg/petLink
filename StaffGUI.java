import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

    private JComponent buildMenu() {
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addPetBtn = new JButton("ADD PET");
        JButton viewPetsBtn = new JButton("VIEW PETS");
        //JButton viewAppsBtn = new JButton("VIEW APPLICATIONS");
        JButton changeLoginBtn = new JButton("CHANGE LOGIN");
        JButton logoutBtn = new JButton("LOGOUT");

        MainGUI.styleButtons(addPetBtn, viewPetsBtn, changeLoginBtn, logoutBtn);

        JButton[] buttons = {addPetBtn, viewPetsBtn, changeLoginBtn, logoutBtn};
        for (JButton b : buttons) {
            b.setPreferredSize(new Dimension(180, 40));
            center.add(b);
        }

        addPetBtn.addActionListener(e -> openAddPetForm());
        viewPetsBtn.addActionListener(e -> viewPets());
        changeLoginBtn.addActionListener(e -> openChangeLoginForm());
        logoutBtn.addActionListener(e -> logout());

        return center;
    }

    private void openChangeLoginForm() {
        String[] actions = {"EMAIL", "PASSWORD"};
        String action = (String) JOptionPane.showInputDialog(this, "CHANGE: ", "CHANGE LOGIN", JOptionPane.PLAIN_MESSAGE, null, actions, actions[0]);
        if (action == null) return;

        JTextField emailField = new JTextField(15);
        JPasswordField newPasswordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);

        Object[] message;

        if ("EMAIL".equals(action)) {
            message = new Object[] {
                "NEW EMAIL:", emailField
            };
        } else { 
            message = new Object[] {
                "NEW PASSWORD:", newPasswordField,
                "CONFIRM PASSWORD:", confirmPasswordField
            };
        }

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                action,
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option != JOptionPane.OK_OPTION) return;

        if ("EMAIL".equals(action)) {
            String newEmail = emailField.getText().trim();
            if (newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "EMAIL CANNOT BE EMPTY", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            userManager.changeUserEmail(this.currentUser.getID(), newEmail);
            JOptionPane.showMessageDialog(this, "EMAIL UPDATED");
            logout();
        } else {
            String newPass = new String(newPasswordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());

            if (newPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "PASS CANNOT BE EMPTY", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "PASSWORDS DO NOT MATCH", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            userManager.changeUserPassword(this.currentUser.getID(), newPass);
            JOptionPane.showMessageDialog(this, "PASSWORD UPDATE");
            logout();
        }
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

    private void viewPets() {
        JFrame f = new JFrame("PETS");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel title = new JLabel("BROWSE PETS", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        container.add(title, BorderLayout.NORTH);

        // Card container
        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JScrollPane scroll = new JScrollPane(cards);
        container.add(scroll, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn   = new JButton("Close");
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        controls.add(refreshBtn);
        controls.add(closeBtn);
        container.add(controls, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadPetCards(cards));
        closeBtn.addActionListener(e -> f.dispose());

        loadPetCards(cards);

        f.setContentPane(container);
        f.setSize(820, 520);
        f.setVisible(true);
    }

    private void loadPetCards(JPanel container) {
        container.removeAll();
        List<Pet> pets = petManager.searchByShelter(currentUser.getParentShelterID());

        if (pets.isEmpty()) {
            JLabel empty = new JLabel("NO PETS FOUND");
            empty.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            container.add(empty);
        } else {
            for (Pet p : pets) {
                container.add(makePetCard(p, () -> loadPetCards(container)));
            }
        }

        container.revalidate();
        container.repaint();
    }

    // Helper to build a pet card
    private JComponent makePetCard(Pet p, Runnable afterSave) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210,210,210)),
            BorderFactory.createEmptyBorder(10,12,10,12)
        ));
        card.setPreferredSize(new Dimension(240, 160));

        JLabel name = new JLabel(p.getName().toUpperCase());
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Smaller labels
        JLabel species = new JLabel("SPECIES: " + p.getSpecies());
        species.setForeground(new Color(90,90,90));
        JLabel breed = new JLabel("BREED: " + p.getBreed());
        breed.setForeground(new Color(90,90,90));
        JLabel age = new JLabel("AGE: " + p.getAge());
        age.setForeground(new Color(90,90,90));
        JLabel temper = new JLabel("TEMPERAMENT: " + p.getTemperament());
        temper.setForeground(new Color(90,90,90));
        JLabel status = new JLabel("STATUS: " + p.getStatus());
        status.setForeground(new Color(120,90,90));

        JButton editBtn = new JButton("EDIT");
        editBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        editBtn.addActionListener(e -> openEditPetDialog(p, afterSave));

        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        species.setAlignmentX(Component.LEFT_ALIGNMENT);
        breed.setAlignmentX(Component.LEFT_ALIGNMENT);
        age.setAlignmentX(Component.LEFT_ALIGNMENT);
        temper.setAlignmentX(Component.LEFT_ALIGNMENT);
        status.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(name);
        card.add(Box.createVerticalStrut(2));
        card.add(species);
        card.add(breed);
        card.add(age);
        card.add(temper);
        card.add(status);
        card.add(Box.createVerticalStrut(4));
        card.add(editBtn);

        return card;
    }

    private void openEditPetDialog(Pet pet, Runnable afterSave) {
        JTextField nameField = new JTextField(pet.getName(), 15);
        JTextField speciesField = new JTextField(pet.getSpecies(), 15);
        JTextField breedField = new JTextField(pet.getBreed(), 15);
        JTextField ageField = new JTextField(String.valueOf(pet.getAge()), 15);
        JTextField temperamentField = new JTextField(pet.getTemperament(), 15);
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"AVAILABLE", "PENDING", "ADOPTED"});
        statusBox.setSelectedItem(pet.getStatus());

        Object[] msg = new Object[]{
                "NAME:", nameField,
                "SPECIES:", speciesField,
                "BREED:", breedField,
                "AGE:", ageField,
                "TEMPERAMENT:", temperamentField,
                "STATUS:", statusBox
        };

        int option = JOptionPane.showConfirmDialog(this, msg, "EDIT: " + pet.getName().toUpperCase(), JOptionPane.OK_CANCEL_OPTION);

        if (option != JOptionPane.OK_OPTION) return;

        petManager.updatePet(pet, nameField.getText(), speciesField.getText(), breedField.getText(), Integer.parseInt(ageField.getText()), temperamentField.getText());
        JOptionPane.showMessageDialog(this, "PET UPDATED");
        if (afterSave != null) afterSave.run();
    }

    private void logout() {
        Window w = SwingUtilities.getWindowAncestor(StaffGUI.this);
        if (w != null) w.dispose();
        SwingUtilities.invokeLater(() -> new MainGUI(userManager, petManager, shelterManager, applicationManager));
    }
}
