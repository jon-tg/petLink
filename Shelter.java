
import java.io.Serializable;
import java.util.*;

public class Shelter implements Serializable{

    private int shelterID;
    private List<Pet> petList;
    private String name;
    private int nextID;
    private List<FosterApplication> recievedApplications;

    public Shelter() {
        Random random = new Random();
        this.shelterID = random.nextInt(9999-1000 + 1) + 1000; //create random 4 digit id 
    }

    public int getShelterID(){
        return this.shelterID;
    }

    public List getPetList(){
        return petList;
    }
    public void setPetList(List<Pet> pets ){
        this.petList = pets;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List getApplications(){
        return this.recievedApplications;
    }

    public void setApplications(List<FosterApplication> applications){
        this.recievedApplications = applications;
    }



}