package microservice.userservice.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

// for the all the common attributes, and we inherit these in required class.
@MappedSuperclass  //for type of inheritance of this class
public class BaseModel {
    @Id//PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
    private Long id;
    private Boolean deleted=false;  // for not making a user inactive if he wants to delete the profile, but we keep the user for future reference by making their profile inactive called as SOFT DELETE is like deactivate  (hard delete is complete removing from db which is kind of DELETING completely from db, as per company policy )

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
