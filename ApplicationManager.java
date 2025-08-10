import java.util.*;
import java.io.*;

public class ApplicationManager {
    // dataFile stores foster application data
    private File dataFile = new File("data/applications.ser");
    private List<Application> applications;

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

    public void addApplication(FosterApplication app) {
        this.applications.add(app);
        saveApplications();
    }

    public boolean removeApplicationById(String applicationID) {
        boolean removed = this.applications.removeIf(a -> a.getApplicationID().equals(applicationID));
        if (removed) saveApplications();
        return removed;
    }

    public FosterApplication findById(String applicationID) {
        for (FosterApplication a : applications) {
            if (a.getApplicationID().equals(applicationID)) return a;
        }
        return null;
    }

    public List<FosterApplication> getAll() {
        return new ArrayList<>(this.applications);
    }

    public List<FosterApplication> getByUser(String userID) {
        return this.applications.stream()
                .filter(a -> a.getUserID().equals(userID))
                .collect(Collectors.toList());
    }

    public List<FosterApplication> getByPet(String petID) {
        return this.applications.stream()
                .filter(a -> a.getPetID().equals(petID))
                .collect(Collectors.toList());
    }

    public List<FosterApplication> getByShelter(String shelterID) {
        return this.applications.stream()
                .filter(a -> a.getShelterID().equals(shelterID))
                .collect(Collectors.toList());
    }

    public List<FosterApplication> getByStatus(String status) {
        return this.applications.stream()
                .filter(a -> a.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public boolean updateStatus(String applicationID, String newStatus) {
        FosterApplication a = findById(applicationID);
        if (a == null) return false;
        a.updateStatus(newStatus);
        saveApplications();
        return true;
    }
}