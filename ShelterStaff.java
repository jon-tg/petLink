public class ShelterStaff extends User {
    private int parentShelterID;

    public ShelterStaff(String email, String password, String name, int shelterID) {
        super(email, password, name);
        this.parentShelterID = shelterID;
        // need something for staff crededentials here
    }

    public int getParentShelterID() {
        return this.parentShelterID;
    }

    public void viewDashboard() {
        System.out.println("Testing");
    }

    // public void addPet(Pet pet){

    // }

    // public void editPet(Pet pet){
        
    // }

    // public void removePet(Pet pet){
        
    // }




}