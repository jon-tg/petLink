public class ShelterStaff extends User {
    private int parentShelterID;

    public ShelterStaff(String email, String password, String name, int shelterID) {
        super(email, password, name);
        this.parentShelterID = shelterID;
    }

    public void viewDashboard() {
        System.out.println("Testing");
    }

    public int getParentShelterID() {
        return this.parentShelterID;
    }
}