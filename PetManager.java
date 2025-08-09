import java.util.*;
import java.io.*;

public class PetManager {

    // dataFile stores pet data
    private File dataFile = new File("/data/pets.ser");
    private List<Pet> pets;

    public PetManager() {
        this.pets = loadPets();
    }

    @SuppressWarnings("unchecked")
    private List<Pet> loadPets() {
        if (!this.dataFile.exists() || dataFile.length() == 0) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.dataFile))) {
            return (List<Pet>) in.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void savePets() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.dataFile))) {
            out.writeObject(this.pets);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPet(Pet p) {
        this.pets.add(p);
        saveUsers();
    }

    public boolean removePetById(int petId) {
        boolean removed = this.pets.removeIf(p -> p.getID() == petID);
        if (removed) savePets();
        return removed;
    }

    public Pet findById(int petId) {
        for (Pet p: pets) {
            if (p.getID() == petId) return p;
        }
        return null;
    }

    public List<Pet> getAll() {
        return new ArrayList<>(this.pets);
    }

    public List<Pet> getAvailable() {
        return this.pets.stream().filter(p -> p.isAvailable()).collect(Collectors.toList());
    }

    public boolean updateStatus(int petId, String newStatus) {
        Pet p = findById(pet Id);
        if (p == null) return false;
        p.changeStatus(newStatus);
        savePets();
        return true;
    }

    public boolean reassignShelter(int petId, Shelter shelter) {
        Pet p = findById(petId);
        if (p == null) return false;
        p.changeShelterLocation(shelter);
        savePets();
        return true;
    }

    public List<Pet> search(String species, String breed, int minAge, int maxAge, String temperament) {
        return pets.stream()
            // Any null search parameters are skipped & not filtered
            .filter(p -> species == null || p.getSpecies().equalsIgnoreCase(species))
            .filter(p -> breed == null || p.getBreed().equalsIgnoreCase(breed))
            .filter(p -> temperament == null || p.getTemperament().equalsIgnoreCase(temperament))
            .filter(p -> minAge == null || p.getAge() >= minAge)
            .filter(p -> maxAge == null || p.getAge <= maxAge)
            .collect(Collectors.toList());
    }
}