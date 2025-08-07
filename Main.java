import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
    }

    private void createUser() {
        UserManager userManager = new UserManager();
        Scanner scanner = new Scanner();
        System.out.println("== PETLINK USER REGISTRATION ==")
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        User user = new User(email, password);
        userManager.addUser(user);
    }
}