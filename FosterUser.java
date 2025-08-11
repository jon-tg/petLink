import java.util.*;

public class FosterUser extends User {
    private List<FosterApplication> fosterApplications;

    public FosterUser(String email, String password, String name) {
        super(email, password, name);
    }

    public void viewDashboard() {
        System.out.println("Testing");
    }

//     public boolean submitApplication(Pet pet) {

//     }

//     public void viewApplications() {
        
//     }

//     public void viewAllPets() {
        
//     }

//     public void viewPetsByShelter() {
        
//     }

}