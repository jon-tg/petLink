import java.io.Serializable; 

public abstract class User implements Serializable {
    private String name;
    private static int userID = 0;
    private String email;
    private String password;
    
    // Creates a new user
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userID = userID++;
    }

    public getID() {
        return this.userID;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public abstract void viewDashboard();
}