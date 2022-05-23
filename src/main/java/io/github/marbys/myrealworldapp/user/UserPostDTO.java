package io.github.marbys.myrealworldapp.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UserPostDTO {

  @NotBlank String username;
  @NotBlank String email;
  @NotBlank String password;
}
