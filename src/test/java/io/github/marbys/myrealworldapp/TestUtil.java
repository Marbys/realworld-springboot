package io.github.marbys.myrealworldapp;

import io.github.marbys.myrealworldapp.user.Profile;
import io.github.marbys.myrealworldapp.user.*;

public class TestUtil {
  public static String USERNAME = "user";
  public static String EMAIL = "user@gmail.com";
  public static String PASSWORD = "password";

  public static Profile sampleProfile() {
    return new Profile(USERNAME, "bio", "image", false);
  }

  public static UserEntity sampleUser() {
    return new UserEntity(PASSWORD, EMAIL, new Profile(USERNAME));
  }

  public static UserModel sampleUserModel() {
    return new UserModel(EMAIL, USERNAME, "token", "", "");
  }

  public static UserLoginDTO sampleLoginDTO() {
    return new UserLoginDTO(EMAIL, PASSWORD);
  }

  public static UserPostDTO samplePostDTO() {
    return new UserPostDTO(USERNAME, EMAIL, PASSWORD);
  }

  public static UserPutDTO samplePutDTO() {
    return new UserPutDTO("new-user@gmail.com", null, null, null, null);
  }
}
