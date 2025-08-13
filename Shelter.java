
import java.util.*;
import java.io.Serializable;

public class Shelter implements Serializable {
    private int shelterID;
    private String joinCode;
    private String name;
    private String address;
    private String state;

    public Shelter(int id, String name, String address, String state) {
        this.shelterID = id;
        this.joinCode = genRandomJoinCode();
        this.name = name;
        this.address = address;
        this.state = state;
    }
    
    public int getShelterID(){
        return this.shelterID;
    }

    public String getJoinCode() {
        return this.joinCode;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getState() {
        return this.state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String genRandomJoinCode() {
        String codeSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            sb.append(codeSet.charAt(random.nextInt(codeSet.length())));
        }
        String joinCode = sb.toString();
        return joinCode;
    }
}