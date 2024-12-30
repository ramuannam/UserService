package microservice.userservice.repositories;

import microservice.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);// its act like upsert= update +  insert i,e,. if user already present then update, or if its a new user insert.


    @Override
    Optional<User> findById(Long id);


    Optional<User> findByEmail(String email);
    
}


