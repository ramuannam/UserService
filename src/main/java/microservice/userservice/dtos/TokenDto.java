package microservice.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import microservice.userservice.models.Token;

import java.util.Date;

@Getter
@Setter
public class TokenDto {
    private String value;
    private Date expiryAt;
    private String email;

    public static TokenDto from(Token token){
        if(token ==null)return null;

        TokenDto tokenDto=new TokenDto();
        tokenDto.setValue(token.getValue());
        tokenDto.setExpiryAt(token.getExpiryAt());
        tokenDto.setEmail(token.getUser().getEmail());
        return tokenDto;

    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
