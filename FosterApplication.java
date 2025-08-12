import java.time.LocalDateTime;
import java.io.Serializable;

public class FosterApplication implements Serializable {
    private static int nextId = 0;
    private String applicationId;
    private String petId;
    private String userId;
    private String shelterId;
    private String status; // can be Pending, Approved, or Rejected
    private LocalDateTime timestamp;

    public FosterApplication(int petID, int userID, int shelterID) {
        this.applicationId = Integer.toString(nextId++);
        this.petId = Integer.toString(petID);
        this.userId = Integer.toString(userID);
        this.shelterId = Integer.toString(shelterID);
        this.status = "Pending";
        this.timestamp = LocalDateTime.now();
    }

    public String getApplicationID() {
        return this.applicationId;
    }

    public String getPetId() {
        return this.petId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getShelterId() {
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