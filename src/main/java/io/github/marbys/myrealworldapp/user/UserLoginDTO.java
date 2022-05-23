package io.github.marbys.myrealworldapp.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@JsonTypeName("user")
@Value
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UserLoginDTO {

  @NotBlank String email;
  @NotBlank String password;
}
