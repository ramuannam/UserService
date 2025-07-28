package microservice.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;



@Getter
@Setter
@Entity(name="tokens") // This line is part of a JPA (Java Persistence API) entity class — it's used to map Java classes to database tables.
//>>🔹@Entity:
// Marks the class as a JPA entity — meaning:
// It will be mapped to a table in the database.
// Each instance of the class will be a row in that table.

//>>🔹 name = "tokens":
// This is an optional attribute of the @Entity annotation.
// It sets the JPA entity name, which can be used in JPQL (Java Persistence Query Language) queries — not the actual database table name.
// eg:
// @Entity(name = "tokens")
// public class Token {
//     @Id
//     private Long id;
// }
// Even though your class is Token, you can now write:
// Query q = entityManager.createQuery("SELECT t FROM tokens t");
// Instead of:
// SELECT t FROM Token t


//>>🔁 But… this doesn't change the actual table name
// To explicitly map to a database table called tokens, you should also use:
// @Table(name = "tokens")
    
// So the full version becomes:
// @Entity(name = "tokens") // JPA query name
// @Table(name = "tokens")  // DB table name
// public class Token {
//     @Id
//     private Long id;
//     // other fields
// }
public class Token extends BaseModel{
    private String value;
    private Date expiryAt;
    @ManyToOne
    private User user;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(Date expiryAt) {
        this.expiryAt = expiryAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
