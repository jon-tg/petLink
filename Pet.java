import java.io.Serializable; 

public class Pet implements Serializable {
    private static int nextId = 0;
    private int petID;
    private String name;
    private String species;
    private String breed;
    private int age;
    private String temperament;
    private String status;
    private int shelterID;

    public Pet(String name, String species, String breed, int age, String temperament, int shelterID) {
        this.petID = nextId++;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.temperament = temperament;
        this.status = "Available";
        this.shelterID = shelterID;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeSpecies(String species) {
        this.species = species;
    }

    public void changeBreed(String breed) {
        this.breed = breed;
    }

    public void changeAge(int age) {
        this.age = age;
    }

    public void changeTemperament(String temperament) {
        this.temperament = temperament;
    }

    public void changeStatus(String status) {
        this.status = status;
    }

    public void changeShelterID(int shelterID) {
        this.shelterID = shelterID;
    }

    public int getID() {
        return this.petID;
    }

    public String getName() {
        return this.name;
    }

    public String getSpecies() {
        return this.species;
    }

    public String getBreed() {
        return this.breed;
    }

    public int getAge() {
        return this.age;
    }

    public String getTemperament() {
        return this.temperament;
    }

    public String getStatus() {
        return this.status;
    }

    public int getShelterID() {
        return this.shelterID;
    }

    public boolean isAvailable() {
        if (this.status.equals("Available")) return true;
        return false;
    }
}