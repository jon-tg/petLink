import java.time.LocalDateTime;
import java.io.Serializable;

public class FosterApplication implements Serializable {
    private static int nextId = 0;
    private int applicationId;
    private int petId;
    private int userId;
    private int shelterId;
    private String status; // can be Pending, Approved, or Rejected
    private LocalDateTime timestamp;

    public FosterApplication(int petID, int userID, int shelterID) {
        this.applicationId = nextId++;
        this.petId = petID;
        this.userId = userID;
        this.shelterId = shelterID;
        this.status = "Pending";
        this.timestamp = LocalDateTime.now();
    }

    public int getApplicationID() {
        return this.applicationId;
    }

    public int getPetId() {
        return this.petId;
    }

    public int getUserId() {
        return this.UserId;
    }

    public int getShelterId() {
        return this.shelterId;
    }

    public String getStatus() {
        return this.status;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void updateStatus(String newStatus) {
        if (newStatus.equals("Pending") || newStatus.equals("Approved") || newStatus.equals("Rejected")) {
            this.status = newStatus;
        }
        else {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }
    }
}