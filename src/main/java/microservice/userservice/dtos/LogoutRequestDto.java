package microservice.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import microservice.userservice.models.Token;

@Getter
@Setter
public class LogoutRequestDto {
    //IN INPUT we only get token value., so in token table we have token value and with token value we can access all other parameters.
    private String tokenValue;

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
