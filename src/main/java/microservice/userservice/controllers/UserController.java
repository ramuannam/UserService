package microservice.userservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import microservice.userservice.dtos.*;
import microservice.userservice.exceptions.ValidTokenNotFoundException;
import microservice.userservice.models.Token;
import microservice.userservice.models.User;
import microservice.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    ///////////////////////////////////1.LOGIN
    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginRequestDto requestDto){
        Token token= userService.login(
                requestDto.getEmail(),
                requestDto.getPassword()
        );
        //converting token to tokenDto (as per the required info for the user we only show that info)
        return TokenDto.from(token);
    }

    //////////////////////////////////2.SIGNUP
    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignUpRequestDto signUpRequestDto) throws JsonProcessingException {

        User user=userService.signUp(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword()
        );
        return  UserDto.from(user); //converting stored Db user to userDto user to avoid password sending.

    }

    ////////////////////////////////3.LOGOUT
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto){ // jus we get a token value that we have to be invalidated.

        ResponseEntity<Void> responseEntity=null;
        try{
            userService.logout(requestDto.getTokenValue());
            responseEntity=new ResponseEntity<>(
                    HttpStatus.OK
            );
        }catch (ValidTokenNotFoundException e){
//            throw new RuntimeException(e);
            responseEntity=new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST
            );
        }

        return responseEntity;
        //todo: move the above exception handling logic into controllerAdvice (global exception handler)
    }


    ////////////////////////////////4.VALIDATE
    @GetMapping("/validate/{tokenValue}")
    public UserDto validateToken(@PathVariable String tokenValue){
        try{
            User user=userService.validateToken(tokenValue);
            return UserDto.from(user);
        }catch (ValidTokenNotFoundException e){
            return null;
//            throw new RuntimeException(e);

        }

    }

}
