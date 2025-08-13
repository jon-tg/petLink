import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class PetManager {
    // dataFile stores pet data
    private File dataFile = new File("data/pets.ser");
    private int nextId;
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
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.dataFile))) {
            reseed();
            out.writeObject(this.pets);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPet(String name, String species, String breed, int age, String temperament, int shelterID) {
        Pet p = new Pet(this.nextId, name, species, breed, age, temperament, shelterID);
        this.pets.add(p);
        savePets();
    }

    public boolean removePetById(int petId) {
        boolean removed = this.pets.removeIf(p -> p.getID() == petId);
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

    private void reseed() {
        int max = pets.stream().mapToInt(Pet::getID).max().orElse(0);
        if (nextId <= max) this.nextId = max + 1;
    }

    public List<Pet> getAvailable() {
        return this.pets.stream().filter(p -> p.isAvailable()).collect(Collectors.toList());
    }

    public List<Pet> getAvailableFrom(List<Pet> pets) {
        return pets.stream().filter(p -> p.isAvailable()).collect(Collectors.toList());
    }

    public void updatePet(Pet pet, String name, String species, String breed, int age, String temperament) {
        pet.changeName(name);
        pet.changeSpecies(species);
        pet.changeBreed(breed);
        pet.changeAge(age);
        pet.changeTemperament(temperament);
        savePets();
    }

    public boolean updateStatus(int petId, String newStatus) {
        Pet p = findById(petId);
        if (p == null) return false;
        p.changeStatus(newStatus);
        savePets();
        return true;
    }

    public boolean reassignShelter(int petId, int shelterID) {
        Pet p = findById(petId);
        if (p == null) return false;
        p.changeShelterID(shelterID);
        savePets();
        return true;
    }

    public List<Pet> searchByShelter(int shelterId) {
        return pets.stream().filter(p -> p.getShelterID() == shelterId).collect(Collectors.toList());
    }

    public List<Pet> search(List<Pet> pets, String species, String breed, int minAge, int maxAge, String temperament) {
        boolean useMin = (minAge >= 0);
        boolean useMax = (maxAge >= 0);

        return pets.stream()
            // Any null search parameters are skipped & not filtered
            .filter(p -> species == null || p.getSpecies().equalsIgnoreCase(species))
            .filter(p -> breed == null || p.getBreed().equalsIgnoreCase(breed))
            .filter(p -> temperament == null || p.getTemperament().equalsIgnoreCase(temperament))
            .filter(p -> !useMin || p.getAge() >= minAge)
            .filter(p -> !useMax || p.getAge() <= maxAge)
            .collect(Collectors.toList());
    }
}