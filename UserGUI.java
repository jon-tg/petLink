import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserGUI extends JPanel {
    private final User currentUser;
    private final UserManager userManager;
    private final PetManager petManager;

    public UserGUI(User user, UserManager um, PetManager pm) {
        this.currentUser = user;
        this.userManager = um;
        this.petManager = pm;

        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
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

    private JComponent buildContent() {
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton browsePets = new JButton("Browse All Pets");
        JButton browsePetsByShelter = new JButton("Browse Pets by Shelter");
        JButton viewApplications = new JButton("View Applications");
        JButton changeLogin = new JButton("Change Login");
        center.add(browsePets);
        center.add(browsePetsByShelter);
        center.add(viewApplications);
        center.add(changeLogin);
        return center;
    }
}
