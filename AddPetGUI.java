import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddPetGUI {
    private final PetManager petManager;
    private final ShelterStaff currentUser;
    private final StaffGUI parentGUI;

    public AddPetGUI(PetManager pm, ShelterStaff cu, StaffGUI pGUI) {
        this.petManager = pm;
        this.currentUser = cu;
        this.parentGUI = pGUI;
    }

    public void displayAddPet() {
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

        int option = JOptionPane.showConfirmDialog(this.parentGUI, msg, "ADD PET", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String name = nameField.getText();
        String species = speciesField.getText();
        String breed = breedField.getText();
        int age = Integer.parseInt(ageField.getText());
        String temperament = temperamentField.getText();

        if (name.isEmpty() || species.isEmpty() || breed.isEmpty() || age < 0 || temperament.isEmpty()) {
            JOptionPane.showMessageDialog(this.parentGUI, "MUST ENTER ALL FIELDS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        petManager.addPet(name, species, breed, age, temperament, currentUser.getParentShelterID());
        JOptionPane.showMessageDialog(this.parentGUI, "PET ADDED");
    }
}
