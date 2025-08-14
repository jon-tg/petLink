import javax.swing.*;
import java.awt.*;

public class ViewPetsGUI {
    private final Component parentComponent;
    private final CardStrategy cs;

    public ViewPetsGUI(Component parentComponent, FosterUser fosterUser, PetManager petManager, ShelterManager shelterManager, ApplicationManager applicationManager) {
        this.parentComponent = parentComponent;
        this.cs = new CardStrategy(fosterUser, petManager, shelterManager, parentComponent, applicationManager);
    }

    public ViewPetsGUI(Component parentComponent, ShelterStaff shelterUser, PetManager petManager, ShelterManager shelterManager, ApplicationManager applicationManager, JPanel cardContainer) {
        this.parentComponent = parentComponent;
        this.cs = new CardStrategy(shelterUser, petManager, shelterManager, parentComponent, applicationManager, cardContainer);
    }

    @SuppressWarnings("unused")
    public void showDialog() {
        JFrame frame = new JFrame("BROWSE PETS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel title = new JLabel("BROWSE AVAILABLE PETS", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        container.add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        container.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        controlsPanel.add(refreshButton);
        controlsPanel.add(closeButton);
        container.add(controlsPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> cs.loadPetCards(cardsPanel));
        closeButton.addActionListener(e -> frame.dispose());

        cs.loadPetCards(cardsPanel);

        frame.setContentPane(container);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(parentComponent);
        frame.setVisible(true);
    }
}