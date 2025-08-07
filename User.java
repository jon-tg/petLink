public class User {
    int userID;
    String email;
    String password;
    
    // Creates a new user
    User(String userID, String email, String password) {
        this.userID = userID;
        this.email = email;
        this.password = password;
    }
    
    boolean login(String email, String password) {
        int valid = 1;
        if (this.email != email) {
            valid = 0;
            print("Email is incorrect");
        }
        else if (this.password != password) {
            valid = 0;
            print("Password is incorrect");
        }
        return valid;
    }

    void viewDashboard() {
        print("TBD");
    }

}