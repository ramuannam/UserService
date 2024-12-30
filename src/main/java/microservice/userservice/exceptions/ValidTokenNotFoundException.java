package microservice.userservice.exceptions;

public class ValidTokenNotFoundException extends RuntimeException {
    public ValidTokenNotFoundException(String message) {
        super(message);
    }
}
