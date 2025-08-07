import java.util.*;
import java.io.*;

public class UserManager {

    // dataFile stores user login credentials
    private File dataFile = new File("users.ser");
    private List<User> users;

    public UserManager() {
        this.users = loadUsers();
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
        if (!this.dataFile.exists() || dataFile.length() == 0) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (List<User>) in.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(users);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(User u) {
        users.add(u);
        saveUsers();
    }

    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    public User login(String email, String password) {
        User loggingIn = null;
        for(User u : this.users) {
            if (email == u.getEmail() && password == u.getPassword()) {
                loggingIn = u;
            }
        }
        return loggingIn;
    }
}