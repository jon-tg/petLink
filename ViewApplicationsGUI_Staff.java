import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class ViewApplicationsGUI_Staff {
    private final ShelterStaff currentUser;
    private final UserManager userManager;
    private final PetManager petManager;
    private final ApplicationManager applicationManager;
    private final JPanel parentComponent;

    public ViewApplicationsGUI_Staff(ShelterStaff user, UserManager um, PetManager pm, ApplicationManager am, JPanel parentComponent) {
        this.currentUser = user;
        this.userManager = um;
        this.petManager = pm;
        this.applicationManager = am;
        this.parentComponent = parentComponent;
    }

    @SuppressWarnings("unused")
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
                JOptionPane.showMessageDialog(parentComponent, "APPLICATION APPROVED");
                if (afterSave != null) afterSave.run();
            }
        });

        rejectBtn.addActionListener(e -> {
            if (confirm("REJECT APPLICATION?")) {
                applicationManager.updateStatus(app.getApplicationID(), "REJECTED");
                petManager.updateStatus(app.getPetId(), "AVAILABLE");
                JOptionPane.showMessageDialog(parentComponent, "APPLICATION REJECTED");
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
                parentComponent, msg, "CONFIRM", JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION;
    }
}