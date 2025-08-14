import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.*;

public class ViewApplicationsGUI {
    private final FosterUser currentUser;
    private final UserManager userManager;
    private final PetManager petManager;
    private final ApplicationManager applicationManager;

    public ViewApplicationsGUI(FosterUser user, UserManager um, PetManager pm, ApplicationManager am) {
        this.currentUser = user;
        this.userManager = um;
        this.petManager = pm;
        this.applicationManager = am;
    }

    public void showApplicationsGUI() {
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

        JButton closeBtn   = new JButton("Close");
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        controls.add(closeBtn);
        container.add(controls, BorderLayout.SOUTH);

        closeBtn.addActionListener(e -> f.dispose());

        loadApplicationCards(cards);

        f.setContentPane(container);
        f.setSize(900, 560);
        f.setVisible(true);
    }

    private void loadApplicationCards(JPanel container) {
        container.removeAll();

        List<FosterApplication> apps = applicationManager.getByUser(this.currentUser.getID());

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

        card.add(heading);
        card.add(Box.createVerticalStrut(2));
        card.add(petLbl);
        card.add(userLbl);
        card.add(submittedLbl);
        card.add(statusLbl);
        card.add(Box.createVerticalStrut(6));

        return card;
    }
}
