import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class ApplicationManager {
    // dataFile stores foster application data
    private File dataFile = new File("data/applications.ser");
    private int nextId;
    private List<FosterApplication> applications;

    public ApplicationManager() {
        this.applications = loadApplications();
    }

    @SuppressWarnings("unchecked")
    private List<FosterApplication> loadApplications() {
        if (!this.dataFile.exists() || dataFile.length() == 0) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.dataFile))) {
            return (List<FosterApplication>) in.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveApplications() {
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.dataFile))) {
            out.writeObject(this.applications);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addApplication(int petID, int userID, int shelterID) {
        FosterApplication app = new FosterApplication(this.nextId, petID, userID, shelterID);
        this.applications.add(app);
        saveApplications();
    }

    private void reseed() {
        int max = applications.stream().mapToInt(FosterApplication::getApplicationID).max().orElse(0);
        if (nextId <= max) this.nextId = max + 1;
    }

    public boolean removeApplicationById(int applicationID) {
        boolean removed = this.applications.removeIf(a -> a.getApplicationID() == applicationID);
        if (removed) saveApplications();
        return removed;
    }

    public FosterApplication findById(int applicationID) {
        for (FosterApplication a : applications) {
            if (a.getApplicationID() == applicationID) return a;
        }
        return null;
    }

    public List<FosterApplication> getAll() {
        return new ArrayList<>(this.applications);
    }

    public List<FosterApplication> getByUser(int userID) {
        return this.applications.stream()
                .filter(a -> a.getUserId() == userID)
                .collect(Collectors.toList());
    }

    public List<FosterApplication> getByPet(int petID) {
        return this.applications.stream()
                .filter(a -> a.getPetId() == petID)
                .collect(Collectors.toList());
    }

    public List<FosterApplication> getByShelter(int shelterID) {
        return this.applications.stream()
                .filter(a -> a.getShelterId() == shelterID)
                .collect(Collectors.toList());
    }

    public List<FosterApplication> getByStatus(String status) {
        return this.applications.stream()
                .filter(a -> a.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public boolean updateStatus(int applicationID, String newStatus) {
        FosterApplication a = findById(applicationID);
        if (a == null) return false;
        a.updateStatus(newStatus);
        saveApplications();
        return true;
    }
}