public class ShelterStaff extends User {
    private int parentShelterID;
    private ShelterManager shelterManager;
    private PetManager petManager;

    public ShelterStaff(String email, String password, String name, int shelterID) {
        super(email, password, name);
        this.parentShelterID = shelterID;
        // TODO add shelter manager 
        // need something for staff crededentials here
    }

    public int getParentShelterID() {
        return this.parentShelterID;
    }

    public void viewDashboard() {
        System.out.println("Testing");
    }

    public void addPet(Pet pet){
      petManager.addPet(pet);
    }

    // public void editPet(Pet pet){
        
    // }

    public void removePet(Pet pet){
        petManager.removePetById(pet.getID());
    }




}