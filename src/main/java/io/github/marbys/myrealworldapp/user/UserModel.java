package io.github.marbys.myrealworldapp.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
public class UserModel {

  private String email;
  private String username;
  private String token;
  private String bio;
  private String image;

  public static UserModel fromEntityAndToken(UserEntity entity, String token) {
    String bio = entity.getProfile().getBio() == null ? "" : entity.getProfile().getBio();
    String image = entity.getProfile().getImage() == null ? "" : entity.getProfile().getImage();
    return new UserModel(entity.getEmail(), entity.getProfile().getUsername(), token, bio, image);
  }

  public static UserModel fromEntity(UserEntity entity) {
    String bio = entity.getProfile().getBio() == null ? "" : entity.getProfile().getBio();
    String image = entity.getProfile().getImage() == null ? "" : entity.getProfile().getImage();
    return new UserModel(entity.getEmail(), entity.getProfile().getUsername(), null, bio, image);
  }
}
