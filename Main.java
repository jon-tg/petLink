import java.util.*;
import javax.swing.*;

public class Main {

    private static UserManager userManager = new UserManager();

    public static void main(String[] args) {
        new MainGUI(userManager);
    }

}