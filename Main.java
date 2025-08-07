import java.util.*;

public class Main {

    private static UserManager userManager = new UserManager();

    public static void main(String[] args) {
        createUser();
        List<User> users= userManager.getAll();
        for (User user: users) {
            System.out.println(user);
            System.out.println("Email: " + user.getEmail());
            System.out.println("Password: " + user.getPassword());
        }
    }

    private static void createUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("== PETLINK USER REGISTRATION ==");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        FosterUser user = new FosterUser(email, password);
        userManager.addUser(user);
    }
}