import java.awt.*;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CardStrategy {
    private final FosterUser fosterUser;
    private final ShelterStaff shelterUser;
    private final ShelterManager shelterManager;
    private final ApplicationManager applicationManager;
    private final Component parentComponent;
    private final PetManager petManager;
    private final JPanel cardContainer;

    public CardStrategy(FosterUser fosterUser, PetManager petManager, ShelterManager shelterManager, Component parentComponent, ApplicationManager applicationManager) {
        this.petManager = petManager;
        this.shelterManager = shelterManager;
        this.fosterUser = fosterUser;
        this.shelterUser = null;
        this.parentComponent = parentComponent;
        this.applicationManager = applicationManager;
        this.cardContainer = null;
    }

    public CardStrategy(ShelterStaff shelterUser, PetManager petManager, ShelterManager shelterManager, Component parentComponent, ApplicationManager applicationManager, JPanel cardContainer) {
        this.petManager = petManager;
        this.shelterManager = shelterManager;
        this.fosterUser = null;
        this.shelterUser = shelterUser;
        this.parentComponent = parentComponent;
        this.applicationManager = applicationManager;
        this.cardContainer = cardContainer;
    }

    public void loadPetCards(JPanel container) {
        if (fosterUser != null) {
            userLoadPets(container);
        } else if (shelterUser != null) {
            staffLoadPets(container);
        }
    }

    private void userLoadPets(JPanel container) {
        container.removeAll();
        List<Pet> availablePets = petManager.getAvailable();

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

    @SuppressWarnings("unused")
    private JComponent createPetCard(Pet pet, Runnable refreshCallback) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setPreferredSize(new Dimension(280, 220));
        card.setMaximumSize(new Dimension(280, 220));

        JLabel nameLabel = new JLabel(pet.getName().toUpperCase());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(new Color(60, 60, 60));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        Shelter shelter = shelterManager.findById(pet.getShelterID());
        String shelterName = (shelter != null) ? shelter.getName() : "Unknown Shelter";
        JLabel shelterLabel = new JLabel("SHELTER: " + shelterName.toUpperCase());
        shelterLabel.setForeground(new Color(90, 90, 90));
        shelterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton applyButton = new JButton("APPLY TO ADOPT");
        applyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyButton.setMaximumSize(new Dimension(200, 30));
        applyButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        boolean hasApplied = hasUserAppliedForPet(fosterUser.getID(), pet.getID());
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
            applicationManager.addApplication(pet.getID(), fosterUser.getID(), pet.getShelterID());
            petManager.updateStatus(pet.getID(), "Pending");
            
            JOptionPane.showMessageDialog(
                parentComponent,
                "Application submitted successfully for " + pet.getName() + "!",
                "Application Submitted",
                JOptionPane.INFORMATION_MESSAGE
            );
            
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

    private void staffLoadPets(JPanel container) {
        container.removeAll();
        List<Pet> pets = petManager.searchByShelter(shelterUser.getParentShelterID());

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
    
        @SuppressWarnings("unused")
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
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"AVAILABLE", "ADOPTED"});
        statusBox.setSelectedItem(pet.getStatus());

        Object[] msg = new Object[]{
                "NAME:", nameField,
                "SPECIES:", speciesField,
                "BREED:", breedField,
                "AGE:", ageField,
                "TEMPERAMENT:", temperamentField,
                "STATUS:", statusBox
        };

        int option = JOptionPane.showConfirmDialog(cardContainer, msg, "EDIT: " + pet.getName().toUpperCase(), JOptionPane.OK_CANCEL_OPTION);

        if (option != JOptionPane.OK_OPTION) return;

        petManager.updatePet(pet, nameField.getText(), speciesField.getText(), breedField.getText(), Integer.parseInt(ageField.getText()), temperamentField.getText());
        JOptionPane.showMessageDialog(cardContainer, "PET UPDATED");
        if (afterSave != null) afterSave.run();
    }
}
