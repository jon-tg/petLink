import java.io.*;
import java.util.*;

public class ShelterManager {
    private File dataFile = new File("data/shelters.ser");
    private int nextId = 1;
    private List<Shelter> shelters;

    public ShelterManager() {
        this.shelters = loadShelters();
    }

    @SuppressWarnings("unchecked")
    private List<Shelter> loadShelters() {
        if (!this.dataFile.exists() || dataFile.length() == 0) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.dataFile))) {
            reseed();
            return (List<Shelter>) in.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveShelters() {
        File parent = this.dataFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.dataFile))) {
            out.writeObject(this.shelters);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addShelter(String name, String address, String state) {
        Shelter s = new Shelter(nextId++, name, address, state);
        shelters.add(s);
        saveShelters();
    }

    private void reseed() {
        int max = shelters.stream().mapToInt(Shelter::getShelterID).max().orElse(0);
        if (nextId <= max) this.nextId = max + 1;
    }

    public Shelter findById(int id) {
        for (Shelter s : shelters) {
            if (s.getShelterID() == id) return s;
        }
        return null;
    }

    public Shelter findByJoinCode(String code) {
        for (Shelter s : shelters) {
            if (s.getJoinCode().equals(code)) return s;
        }
        return null;
    }

    public Shelter findByName(String name) {
        for (Shelter s : shelters) {
            if (s.getName().equals(name)) return s;
        }
        return null;
    }

    public List<Shelter> getAll() {
        return new ArrayList<>(shelters);
    }

    public boolean removeShelter(int id) {
        boolean removed = shelters.removeIf(s -> s.getShelterID() == id);
        if (removed) saveShelters();
        return removed;
    }
}