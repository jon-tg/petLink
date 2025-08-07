import java.io.Serializable; 

public abstract class User implements Serializable {
    private String email;
    private String password;
    
    // Creates a new user
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
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

   public  void viewDashboard() {
        System.out.print("TBD");
    }

}