import java.util.*;
import javax.swing.*;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    private final static UserManager userManager = new UserManager();
    private final static PetManager petManager = new PetManager();
    private final static ShelterManager shelterManager = new ShelterManager();

    public static void main(String[] args) {
        new MainGUI(userManager, petManager, shelterManager);
    }
}