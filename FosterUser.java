import java.util.*;

public class FosterUser extends User {
    private List<FosterApplication> fosterApplications;
    private ApplicationManager applicationManager;

    public FosterUser(String email, String password, String name) {
        super(email, password, name);
    }

    public void viewDashboard() {
        System.out.println("Testing");
    }

    public boolean submitApplication(Pet pet) {
        FosterApplication application = new FosterApplication(pet.getID(), this.getID(), pet.getShelterID());
        applicationManager.addApplication(application);
        return true;
    }

    public void viewApplications() {
        applicationManager.getAll();
    }

    // public void viewAllPets() {
    //     Shelter curr = shelterManager.getAll()
    // }

//     public void viewPetsByShelter() {
        
//     }

}