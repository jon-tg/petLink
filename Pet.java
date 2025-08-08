import java.io.Serializable; 

public class Pet implements Serializable {
    private static int petID = 0;
    private String name;
    private String species;
    private String breed;
    private int age;
    private String temperament;
    private String status;
    private Shelter shelterLocation;

    public Pet(String name, String species, String breed, int age, String temperament, Shelter shelterLocation) {
        this.petID = petID++;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.temperament = temperament;
        this.status = "Available";
        this.shelterLocation = shelterLocation;
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

    public void changeShelterLocation(Shelter shelter) {
        this.shelterLocation = shelter;
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

    public Shelter getShelterLocation() {
        return this.shelterLocation;
    }

    public boolean isAvailable() {
        if (this.status.equals("Available")) return true;
        return false;
    }
}