import java.util.*;
import javax.swing.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();
    private static PetManager petManager = new PetManager();
    private static ApplicationManager applicationManager = new ApplicationManager();

    public static void main(String[] args) {
        new MainGUI(userManager);
    }
}