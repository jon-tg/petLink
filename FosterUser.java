import java.util.*;

public class FosterUser extends User {
    public FosterUser(String email, String password, String name) {
        super(email, password, name);
    }

    public void viewDashboard() {
        System.out.println("Testing");
    }

}