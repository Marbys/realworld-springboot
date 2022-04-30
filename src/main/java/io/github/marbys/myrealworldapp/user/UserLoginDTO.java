package io.github.marbys.myrealworldapp.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

@JsonTypeName("user")
@Value
public class UserLoginDTO {

    private String email;
    private String password;
}
