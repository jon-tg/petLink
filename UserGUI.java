import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

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
        changeLogin.addActionListener(e -> openChangeLoginForm());
        logout.addActionListener(e -> logout());
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

    private void viewShelters() {
        JFrame f = new JFrame("SHELTERS");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel title = new JLabel("VIEW ALL SHELTERS: ", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(title, BorderLayout.NORTH);

        // Card container
        JPanel cards = new JPanel();
        cards.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
        JScrollPane scroll = new JScrollPane(cards);
        container.add(scroll, BorderLayout.CENTER);

        // Control buttons
        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn   = new JButton("Close");
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        controls.add(refreshBtn);
        controls.add(closeBtn);
        container.add(controls, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadShelterCards(cards));
        closeBtn.addActionListener(e -> f.dispose());

        loadShelterCards(cards);
        f.setContentPane(container);
        f.setSize(780, 480);
        f.setVisible(true);
    }

    private void loadShelterCards(JPanel container) {
        container.removeAll();
        List<Shelter> shelters = this.shelterManager.getAll();

        if (shelters.isEmpty()) {
            JLabel empty = new JLabel("NO SHELTERS FOUND");
            empty.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            container.add(empty);
        } else {
            for (Shelter s : shelters) {
                JPanel card = new JPanel(new GridLayout(0,1,0,2));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210,210,210)),
                    BorderFactory.createEmptyBorder(10,12,10,12)
                ));
                JLabel name = new JLabel(s.getName().toUpperCase());
                name.setFont(new Font("Segoe UI", Font.BOLD, 16));
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.add(name);
                card.add(new JLabel("ADDRESS: " + s.getAddress().toUpperCase()));
                card.add(new JLabel("STATE: " + s.getState().toUpperCase()));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);

                container.add(card);
                container.add(Box.createVerticalStrut(8));
            }
        }
        container.revalidate();
        container.repaint();
    }

    private void viewPets() {
        List<Shelter> shelters = shelterManager.getAll();
        List<String> options = new ArrayList<>();
        
        // Search shelters with available pets
        for (Shelter s : shelters) {
            List<Pet> byShelter = petManager.searchByShelter(s.getShelterID());
            List<Pet> avail = petManager.getAvailableFrom(byShelter);
            if (!avail.isEmpty()) {
                options.add(s.getName());
            }
        }
        if (options.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NO SHELTERS WITH PETS");
            return;
        }
        String[] names = options.toArray(new String[0]);
        String c = (String) JOptionPane.showInputDialog(this, "CHOOSE A SHELTER: ", "SELECT SHELTER", JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
        if (c == null) return;
        Shelter choice = shelterManager.findByName(c);
        int shelterId = choice.getShelterID();

        JFrame f = new JFrame("PETS");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        JLabel title = new JLabel(("BROWSE PETS — " + choice.getName()).toUpperCase(), SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        container.add(title, BorderLayout.NORTH);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JTextField speciesField = new JTextField(10);
        JTextField breedField = new JTextField(10);
        JTextField temperField = new JTextField(10);
        JTextField minAgeField = new JTextField(4);
        JTextField maxAgeField = new JTextField(4);

        filters.add(new JLabel("Species:"));     
        filters.add(speciesField);
        filters.add(new JLabel("Breed:"));       
        filters.add(breedField);
        filters.add(new JLabel("Temperament:")); 
        filters.add(temperField);
        filters.add(new JLabel("Age min:"));     
        filters.add(minAgeField);
        filters.add(new JLabel("Age max:"));     
        filters.add(maxAgeField);

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JScrollPane scroll = new JScrollPane(cards);

        JPanel south = new JPanel(new BorderLayout(8, 8));
        south.add(filters, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");
        actions.add(searchBtn);
        actions.add(refreshBtn);
        actions.add(closeBtn);
        south.add(actions, BorderLayout.EAST);

        container.add(scroll, BorderLayout.CENTER);
        container.add(south, BorderLayout.SOUTH);

        // Get results from search filters & shelter chosen
        final Runnable load = new Runnable() {
            @Override public void run() {
                cards.removeAll();
                List<Pet> base = petManager.getAvailableFrom(petManager.searchByShelter(shelterId));

                int min = -1;
                int max = -1;
                try {
                    String t = minAgeField.getText().trim();
                    if (!t.isEmpty()) min = Integer.parseInt(t);
                    t = maxAgeField.getText().trim();
                    if (!t.isEmpty()) max = Integer.parseInt(t);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(UserGUI.this, "AGE MUST BE NUMBER");
                    return;
                }

                String species = speciesField.getText().trim();
                if (species.isEmpty()) species = null;
                String breed = breedField.getText().trim();
                if (breed.isEmpty()) breed = null;
                String temper = temperField.getText().trim();
                if (temper.isEmpty()) temper = null;

                List<Pet> filtered = petManager.search(base, species, breed, min, max, temper);

                if (filtered.isEmpty()) {
                    JLabel empty = new JLabel("NO PETS FOUND");
                    empty.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                    cards.add(empty);
                } else {
                    for (Pet p : filtered) {
                        cards.add(makePetCard(p, this));
                    }
                }
                cards.revalidate();
                cards.repaint();
            }
        };

        searchBtn.addActionListener(e -> load.run());
        refreshBtn.addActionListener(e -> {
            speciesField.setText(""); 
            breedField.setText(""); 
            temperField.setText("");
            minAgeField.setText(""); 
            maxAgeField.setText("");
            load.run();
        });
        closeBtn.addActionListener(e -> f.dispose());
        load.run();
        f.setContentPane(container);
        f.setSize(880, 580);
        f.setLocationByPlatform(true);
        f.setVisible(true);
    }

    private JComponent makePetCard(Pet p, Runnable afterApply) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210,210,210)),
                BorderFactory.createEmptyBorder(10,12,10,12)
        ));
        card.setPreferredSize(new Dimension(240, 180));

        JLabel name = new JLabel((p.getName() == null ? "" : p.getName()).toUpperCase());
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel species = smallGray("SPECIES: " + p.getSpecies());
        JLabel breed   = smallGray("BREED: "   + p.getBreed());
        JLabel age     = smallGray("AGE: "     + p.getAge());
        JLabel temper  = smallGray("TEMPERAMENT: " + p.getTemperament());
        JLabel status  = smallGray("STATUS: "  + p.getStatus());

        JButton applyBtn = new JButton("APPLY TO FOSTER");
        applyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "SEND APPLICATION FOR: " + p.getName().toUpperCase() + "?",
                    "CONFIRM",
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (confirm != JOptionPane.OK_OPTION) return;

            try {
                applicationManager.addApplication(currentUser.getID(), p.getID(), p.getShelterID());
                JOptionPane.showMessageDialog(this, "APPLICATION SUBMITTED!");
                p.changeStatus("Pending");
                if (afterApply != null) afterApply.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "FAILED TO SUBMIT APPLICATION: " + ex.getMessage());
            }
        });

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
        card.add(Box.createVerticalStrut(6));
        card.add(applyBtn);
        return card;
    }

    private JLabel smallGray(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(90,90,90));
        return l;
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