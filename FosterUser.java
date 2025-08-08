import java.util.*;

public class FosterUser extends User {

    private static int fosterUserID = 0;

    public FosterUser(String email, String password, String name) {
        super(email, password, name);
        this.fosterUserID = fosterUserID++;
    }

    public int getID() {
        return this.fosterUserID;
    }

    public void viewDashboard() {
       
    }

}