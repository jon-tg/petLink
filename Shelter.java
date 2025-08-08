
import java.util.*;

public class Shelter{

    private int shelterID;
    private List<Pet> petList;

    public Shelter() {
        Random random = new Random();
        this.shelterID = random.nextInt(9999-1000 + 1) + 1000; //create random 4 digit id 
    }

    public int getShelterID(){
        return this.shelterID;
    }

    


}