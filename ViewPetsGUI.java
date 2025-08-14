import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewPetsGUI {
    private final Component parentComponent;
    private final FosterUser currentUser;
    private final PetManager petManager;
    private final ShelterManager shelterManager;
    private final ApplicationManager applicationManager;

    public ViewPetsGUI(Component parentComponent, FosterUser currentUser, PetManager petManager, 
                       ShelterManager shelterManager, ApplicationManager applicationManager) {
        this.parentComponent = parentComponent;
        this.currentUser = currentUser;
        this.petManager = petManager;
        this.shelterManager = shelterManager;
        this.applicationManager = applicationManager;
    }

    public void showDialog() {
        JFrame frame = new JFrame("BROWSE PETS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        // Header
        JLabel title = new JLabel("BROWSE AVAILABLE PETS", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        container.add(title, BorderLayout.NORTH);

        // Pet cards container
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        container.add(scrollPane, BorderLayout.CENTER);

        // Control buttons
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        controlsPanel.add(refreshButton);
        controlsPanel.add(closeButton);
        container.add(controlsPanel, BorderLayout.SOUTH);

        // Event listeners
        refreshButton.addActionListener(e -> loadPetCards(cardsPanel));
        closeButton.addActionListener(e -> frame.dispose());

        // Load initial pet cards
        loadPetCards(cardsPanel);

        frame.setContentPane(container);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(parentComponent);
        frame.setVisible(true);
    }

    private void loadPetCards(JPanel container) {
        container.removeAll();
        List<Pet> availablePets = petManager.getAvailable(); // Using the correct method name

        if (availablePets == null || availablePets.isEmpty()) {
            JLabel emptyLabel = new JLabel("NO PETS AVAILABLE FOR ADOPTION");
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            container.add(emptyLabel);
        } else {
            for (Pet pet : availablePets) {
                container.add(createPetCard(pet, () -> loadPetCards(container)));
            }
        }

        container.revalidate();
        container.repaint();
    }

    private JComponent createPetCard(Pet pet, Runnable refreshCallback) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setPreferredSize(new Dimension(280, 220));
        card.setMaximumSize(new Dimension(280, 220));

        // Pet name (header)
        JLabel nameLabel = new JLabel(pet.getName().toUpperCase());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(new Color(60, 60, 60));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Pet details
        JLabel speciesLabel = new JLabel("SPECIES: " + pet.getSpecies().toUpperCase());
        speciesLabel.setForeground(new Color(90, 90, 90));
        speciesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel breedLabel = new JLabel("BREED: " + pet.getBreed().toUpperCase());
        breedLabel.setForeground(new Color(90, 90, 90));
        breedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel ageLabel = new JLabel("AGE: " + pet.getAge() + " YEARS");
        ageLabel.setForeground(new Color(90, 90, 90));
        ageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel temperamentLabel = new JLabel("TEMPERAMENT: " + pet.getTemperament().toUpperCase());
        temperamentLabel.setForeground(new Color(90, 90, 90));
        temperamentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Shelter information
        Shelter shelter = shelterManager.findById(pet.getShelterID());
        String shelterName = (shelter != null) ? shelter.getName() : "Unknown Shelter";
        JLabel shelterLabel = new JLabel("SHELTER: " + shelterName.toUpperCase());
        shelterLabel.setForeground(new Color(90, 90, 90));
        shelterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Apply button
        JButton applyButton = new JButton("APPLY TO ADOPT");
        applyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyButton.setMaximumSize(new Dimension(200, 30));
        applyButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Check if user has already applied for this pet
        boolean hasApplied = hasUserAppliedForPet(currentUser.getID(), pet.getID());
        if (hasApplied) {
            applyButton.setEnabled(false);
            applyButton.setText("ALREADY APPLIED");
        }

        applyButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                card,
                "Are you sure you want to apply to adopt " + pet.getName() + "?",
                "Confirm Application",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                handlePetApplication(pet, refreshCallback);
            }
        });

        // Add components to card
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(speciesLabel);
        card.add(breedLabel);
        card.add(ageLabel);
        card.add(temperamentLabel);
        card.add(shelterLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(applyButton);

        return card;
    }

    private boolean hasUserAppliedForPet(int userId, int petId) {
        List<FosterApplication> userApplications = applicationManager.getByUser(userId);
        return userApplications.stream()
                .anyMatch(app -> app.getPetId() == petId);
    }

    private void handlePetApplication(Pet pet, Runnable refreshCallback) {
        try {
            // Create and submit application using the correct ApplicationManager method
            applicationManager.addApplication(pet.getID(), currentUser.getID(), pet.getShelterID());
            
            JOptionPane.showMessageDialog(
                parentComponent,
                "Application submitted successfully for " + pet.getName() + "!",
                "Application Submitted",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Refresh the pet cards to update button states
            refreshCallback.run();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                parentComponent,
                "An error occurred while submitting the application: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}