import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class UserGUI extends JPanel {
    private final User currentUser;
    private final UserManager userManager;
    private final PetManager petManager;
    private final ShelterManager shelterManager;

    public UserGUI(User user, UserManager um, PetManager pm, ShelterManager sm) {
        this.currentUser = user;
        this.userManager = um;
        this.petManager = pm;
        this.shelterManager = sm;

        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildMenu(), BorderLayout.CENTER);
    }

    private JComponent buildHeader() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel title = new JLabel("WELCOME, " + currentUser.getName().toUpperCase(), SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(currentUser.getEmail().toUpperCase());
        sub.setForeground(new Color(110, 100, 110));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(title);
        p.add(sub);
        return p;
    }

    private JComponent buildMenu() {
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton browsePets = new JButton("Browse Pets");
        JButton viewShelters = new JButton("View Shelters");
        JButton viewApplications = new JButton("View Applications");
        JButton changeLogin = new JButton("Change Login");
        JButton logout = new JButton("Logout");
        JButton[] buttons = {browsePets, viewShelters, viewApplications, changeLogin, logout};

        MainGUI.styleButtons(browsePets, viewShelters, viewApplications, changeLogin, logout);
        for (JButton btn: buttons) {
            btn.setPreferredSize(new Dimension(180, 40));
            center.add(btn);
        }

        viewShelters.addActionListener(e -> viewShelters());

        return center;
    }

    private void viewShelters() {
        JFrame f = new JFrame("Shelters");
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
}
