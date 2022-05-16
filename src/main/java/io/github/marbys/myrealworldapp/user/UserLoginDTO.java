package io.github.marbys.myrealworldapp.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@JsonTypeName("user")
@Value
public class UserLoginDTO {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
