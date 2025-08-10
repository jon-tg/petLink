import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();
    private static PetManager petManager = new PetManager();
    private static ApplicationManager applicationManager = new ApplicationManager();

    public static void main(String[] args) {
        createUser();
    }

    private static void createUser() {
        System.out.println("== PETLINK USER REGISTRATION ==");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        FosterUser user = new FosterUser(email, password, name);
        userManager.addUser(user);
    }
}