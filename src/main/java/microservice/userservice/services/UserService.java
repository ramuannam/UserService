package microservice.userservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import microservice.userservice.models.Token;
import microservice.userservice.models.User;

public interface UserService {
   Token login(String email, String password);

   User signUp(String name, String email, String password) throws JsonProcessingException;

   void logout(String token);

    User validateToken(String token);

}
