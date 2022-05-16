package io.github.marbys.myrealworldapp.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Value
@JsonTypeName("user")
public class UserPostDTO {


    @NotBlank private String username;
    @NotBlank private String email;
    @NotBlank private String password;

}
