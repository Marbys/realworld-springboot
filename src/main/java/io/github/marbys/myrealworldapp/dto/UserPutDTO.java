package io.github.marbys.myrealworldapp.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.util.Optional;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UserPutDTO {

  String email;
  String username;
  String password;
  String bio;
  String image;

  public Optional<String> getEmailToUpdate() {
    return Optional.ofNullable(email);
  }

  public Optional<String> getUsernameToUpdate() {
    return Optional.ofNullable(username);
  }

  public Optional<String> getPasswordToUpdate() {
    return Optional.ofNullable(password);
  }

  public Optional<String> getBioToUpdate() {
    return Optional.ofNullable(bio);
  }

  public Optional<String> getImageToUpdate() {
    return Optional.ofNullable(image);
  }
}
