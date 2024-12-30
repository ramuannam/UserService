package microservice.userservice.repositories;

import microservice.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Override
    Token save(Token token);


    //Declared Query
    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String value,boolean deleted, Date expiryAt);

    //case:2 when the token expiry time is greater than current time and the token is not invalidated(i,.e its false)
    //select * from tokens where value=? and deleted = false and expiry_at > current_time

}
