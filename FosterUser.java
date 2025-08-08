import java.util.*;

public class FosterUser extends User {

    private static int fosterUserID = 0;

    public FosterUser(String email, String password) {
        super(email, password);
        this.fosterUserID = fosterUserID++;
    }

    public getID() {
        return this.fosterUserID;
    }

    public void viewDashboard() {
        int option = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("== USER MENU == ");
        System.out.println("1. VIEW ALL FOSTERABLE PETS");
        System.out.println("2. VIEW FOSTER APPLICATIONS");
        System.out.println("Enter an option: ");
        option = scanner.nextInt();
    }



}