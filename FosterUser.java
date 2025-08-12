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
        try {
            FosterApplication application = new FosterApplication(pet.getID(), this.getID(), pet.getShelterID());

            applicationManager.addApplication(application);

            if (fosterApplications == null) {
                fosterApplications = new ArrayList<>();
            } else {
                fosterApplications.add(application);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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