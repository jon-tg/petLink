import java.util.*;
import java.io.*;

public class UserManager {
    // dataFile stores user login credentials
    private File dataFile = new File("data/users.ser");
    private List<User> users;

    public UserManager() {
        this.users = loadUsers();
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
        if (!this.dataFile.exists() || dataFile.length() == 0) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.dataFile))) {
            return (List<User>) in.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveUsers() {
        // Checks existence of data directory (creates one if doesn't exist)
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.dataFile))) {
            out.writeObject(this.users);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addUser(User u) {
        if (u == null || u.getEmail() == null || u.getPassword() == null) return false;
        if (emailExists(u.getEmail())) return false; // Email cannot be in use already
        this.users.add(u);
        saveUsers();
        return true;
    }

    public User findById(int userId) {
        for (User u: this.users) {
            if (u.getID() == userId) return u;
        }
        return null;
    }

    public User findByEmail(String email) {
        if (email == null) return null;
        for (User u: this.users) {
            if (email.equalsIgnoreCase(u.getEmail())) return u;
        }
        return null;
    }

    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    public boolean removeUserById(int userId) {
        boolean removed = users.removeIf(u -> u.getID() == userId);
        if (removed) saveUsers();
        return removed;
    }

    public boolean removeUserByEmail(String email) {
        boolean removed = users.removeIf(u -> email != null && email.equalsIgnoreCase(u.getEmail()));
        if (removed) saveUsers();
        return removed;
    }

    public boolean changeUserEmail(int userId, String newEmail) {
        if (newEmail == null || newEmail.isBlank()) return false;
        if (emailExists(newEmail)) return false; // New email cannot be in use already

        User u = findById(userId);
        if (u == null) return false;

        u.changeEmail(newEmail);
        saveUsers();
        return true;
    }

    public boolean changeUserPassword(int userId, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) return false;

        User u = findById(userId);
        if (u == null) return false;

        u.changePassword(newPassword);
        saveUsers();
        return true;
    }

    public List<User> getAll() {
        return new ArrayList<>(this.users);
    }

    // For debugging
    public void printAllUsers() {
        for (User user : users) {
            System.out.println(user.getName() + " "+ user.getEmail() + " " + user.getPassword());
        }
    }

    public User login(String email, String password) {
        for (User u : this.users) {
            if (email.equals(u.getEmail()) && password.equals(u.getPassword())) {
                return u;
            }
        }
        return null;
    }
}