package microservice.userservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.userservice.dtos.SendEmailDto;
import microservice.userservice.exceptions.UserNotFoundException;
import microservice.userservice.exceptions.ValidTokenNotFoundException;
import microservice.userservice.models.Token;
import microservice.userservice.models.User;
import microservice.userservice.repositories.TokenRepository;
import microservice.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String, String> kafkaTemplate; // <body>, <JSON Data>
    private ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           TokenRepository tokenRepository,
                           KafkaTemplate<String, String> kafkaTemplate,
                           ObjectMapper objectMapper ) {
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
        this.tokenRepository=tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }


    /// ////////////////////////////////1.Login
    @Override
    public Token login(String email, String password) {
                /*
        1.Find the user by email.
        2.If the user is not found, return null(exception with a message).
        3.If the user is present is DB then verify the user with password.
        4. If password matches, then generate the token and return it.
         */

        //step:1 get the user, based on email. if present else return null/exception.
        Optional<User> optionalUser=userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }


        User user=optionalUser.get();


        // Addon-feature: making the login restrictions to only two users.
        //so check the no of tokens for this userId, and check the count of tokens(make sure you have list of tokens).
        // TODO: TO IMPLEMENT THIS.

        Token token=null;
        //step2 Match the password.(for successful login)
        if(passwordEncoder.matches(password,user.getPassword())){ // raw password, db stored password
            //step2.1
            //if matches login successful, So we need to generate the token(as we are the Auth service here we need to generate the token also)(without OAuth)

            token=new Token();
            token.setUser(user);//######1

            //expiry time should be 30 days from current time(now)
            Calendar calendar= Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,30);
            Date date30DaysFromNow=calendar.getTime(); // from here we get time in epoch for 30 days.
            token.setExpiryAt(date30DaysFromNow);//#####2

            //Token value can be any random string of 128 characters.(or 32 or 64 characters)
            // so we use Apache -lang library and need to add as dependency, when we need to work with strings.
            token.setValue(RandomStringUtils.randomAlphanumeric(128));//#####3

        }

        return tokenRepository.save(token); //save and return the token
    }


    //////////////////////////////////////////////////2.Signup
    @Override
    public User signUp(String name, String email, String password) throws JsonProcessingException { //As it's a signup, we need db access to create a user object


        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            //redirect the user to  login page, if user is already present
        }

        //else create new user and assign those user details and store in db
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        //for storing of password we don;t jus store the same plain text we encrypt and store it, using bCrypt Encoder(which uses  adaptive hashing algo).
        user.setPassword(passwordEncoder.encode(password));
        user.setVerified(true);
        //by default delete will be false.



        //USING Dto to send the email details(as it's going to converted to JSON to pass over the network and vice versa using "OBJECT MAPPER" from jackson library)
        SendEmailDto sendEmailDto=new SendEmailDto();
        sendEmailDto.setTo(email);
        sendEmailDto.setSubject("Welcome to scaler");
        sendEmailDto.setBody("Jus hustle and achieve your dreams by using the best resources available, and we are happy to have you onboarded, All the best for your journey.");

        //Publish an event inside kafka to send a WELCOME Email to the user.
        kafkaTemplate.send(
                "sendEmail",   // topic name
                objectMapper.writeValueAsString(sendEmailDto)// to convert object to string in JSON format.

        );

        return userRepository.save(user);  //save user object in db.


    }



    //////////////////////////////////////3.logout
    @Override
    public void logout(String tokenValue) {
        /*
        case:1= user trying to logout
        In logout we try to delete the token (using token value that we get from browser), (for addon: you can check if token is present then delete from the token repository)

         case:2 we tr to logout when a particular token in the db has expiry time exceeds/greater than current time, and deleted is false.

         */

        Optional<Token> optionalToken=
                tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(tokenValue,false,new Date()); //here deleted value is false, because we are searching a token before its logout, so the token should be valid so false.and the token expiry_time should be greater than the current time then only the token is valid else not valid, as token has eg: 30days of expiry time.
        if(optionalToken.isEmpty()) {
            // if token not found
            throw new ValidTokenNotFoundException("Valid token not found");
        }

        //if token found, then we need to logout by making the token as invalid.
        Token token =optionalToken.get();
        token.setDeleted(true);  //made the token deleted,(invalidated by making it as true, which is like soft delete)
        tokenRepository.save(token); //finally save in db

    }


    ///////////////////////////////////////////////4.validateToken
    @Override
    public User validateToken(String tokenValue) {
        Optional<Token> optionalToken=
                tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(tokenValue,false,new Date());
        if(optionalToken.isEmpty()) {
            throw new ValidTokenNotFoundException("Valid token not found");
        }
        Token token=optionalToken.get();


        return token.getUser();
    }
}
