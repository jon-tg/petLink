import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.*;

public class ViewSheltersGUI {
    private final ShelterManager shelterManager;

    public ViewSheltersGUI(ShelterManager shelterManager) {
        this.shelterManager = shelterManager;
    }

    @SuppressWarnings("unused")
    public void showSheltersGUI() {
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
}
