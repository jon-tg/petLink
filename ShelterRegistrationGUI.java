import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ShelterRegistrationGUI {
    public static void showShelterRegistrationDialog(JFrame frame, ShelterManager shelterManager) {
        JTextField nameField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField stateField = new JTextField(30);

        Object[] message = {
            "SHELTER NAME: ", nameField,
            "ADDRESS: ", addressField,
            "STATE: ", stateField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "REGISTER SHELTER", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String address = addressField.getText();
            String state = stateField.getText();

            if (name.isEmpty() || address.isEmpty() || state.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "MUST ENTER ALL REGISTRATION FIELDS!");
                return;
            }

            shelterManager.addShelter(name, address, state);
            Shelter newShelter = shelterManager.findByName(name);
            JOptionPane.showMessageDialog(frame, "SHELTER JOIN CODE: " + newShelter.getJoinCode());
            System.out.println(newShelter.getJoinCode());
        }
    }
}
