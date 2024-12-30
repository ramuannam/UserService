package microservice.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import microservice.userservice.models.Role;
import microservice.userservice.models.User;

import java.util.List;

@Getter
@Setter
public class UserDto { //USER details we send back, without password
    private String name;
    private String email;
//    private List<Role>roles;

    public static UserDto from(User user) {

        if(user==null) return null;//handling nullPointer exception

        UserDto userDto = new UserDto();

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(List<Role> roles) {
//        this.roles = roles;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
