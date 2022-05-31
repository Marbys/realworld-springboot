package io.github.marbys.myrealworldapp;

import io.github.marbys.myrealworldapp.application.dto.UserLoginDTO;
import io.github.marbys.myrealworldapp.application.dto.UserPostDTO;
import io.github.marbys.myrealworldapp.application.dto.UserPutDTO;
import io.github.marbys.myrealworldapp.domain.*;
import io.github.marbys.myrealworldapp.domain.model.UserModel;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TestUtil {
  public static String USERNAME = "user";
  public static String EMAIL = "user@gmail.com";
  public static String PASSWORD = "password";

  public static Profile sampleProfile() {
    return new Profile(USERNAME, "bio", "image", false);
  }

  public static User sampleUser() {
    return new User(PASSWORD, EMAIL, sampleProfile());
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

  public static Set<Tag> sampleTagSet() {
    return new HashSet(Arrays.asList(new Tag("angularJs"), new Tag("dragon")));
  }

  public static ArticleContent sampleArticleContent() {
    return new ArticleContent(
        "How to train your dragon", "Ever wonder how?", "It takes a Jacobian", sampleTagSet());
  }

  public static Article sampleArticle() {
    Article article = new Article(sampleArticleContent(), sampleUser());
    article.setCreatedAt(Instant.now());
    article.setUpdatedAt(Instant.now());
    article.setSlug(
        sampleArticleContent().getTitle().replaceAll(" ", "-").toLowerCase(Locale.ROOT));
    return article;
  }
}
