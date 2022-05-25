package io.github.marbys.myrealworldapp;

import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class IntegrationTestUtils {

  public static ResultMatcher[] validUserModel() {
    return new ResultMatcher[] {
      jsonPath("user").isMap(),
      jsonPath("user.username").isString(),
      jsonPath("user.email").isString(),
      jsonPath("user.bio").isString(),
      jsonPath("user.image").hasJsonPath(),
      jsonPath("user.token").hasJsonPath()
    };
  }

  public static ResultMatcher[] validProfile() {
    return validProfileInPath("profile");
  }

  private static ResultMatcher[] validProfileInPath(String path) {
    return new ResultMatcher[] {
      jsonPath(path + "").isMap(),
      jsonPath(path + ".username").isString(),
      jsonPath(path + ".bio").hasJsonPath(),
      jsonPath(path + ".image").hasJsonPath(),
      jsonPath(path + ".following").isBoolean()
    };
  }

  public static ResultMatcher[] validComment() {
    return combine(
        new ResultMatcher[] {
          jsonPath("comment").isMap(),
          jsonPath("comment.id").isNumber(),
          jsonPath("comment.createdAt").isString(),
          jsonPath("comment.updatedAt").isString(),
          jsonPath("comment.body").isString()
        },
        validProfileInPath("comment.author"));
  }

  public static ResultMatcher[] validSingleArticleModel() {
    return validArticle("article");
  }

  private static ResultMatcher[] validArticle(String path) {
    return combine(
        new ResultMatcher[] {
          jsonPath(path).isMap(),
          jsonPath(path + ".slug").isString(),
          jsonPath(path + ".title").isString(),
          jsonPath(path + ".description").isString(),
          jsonPath(path + ".tagList").isArray(),
          jsonPath(path + ".createdAt").isString(),
          jsonPath(path + ".updatedAt").isString(),
          jsonPath(path + ".favoritesCount").isNumber()
        },
        validProfileInPath(path + ".author"));
  }

  //  public static ResultMatcher[] validSingleArticleModel() {
  //    return combine(
  //            new ResultMatcher[] {jsonPath("articles").isArray(), jsonPath("articlesCount",
  // is(1))},
  //            validArticle("articles[0]"));
  //  }

  public static ResultMatcher[] validMultipleArticleModel() {
    return combine(
        new ResultMatcher[] {jsonPath("articles").isArray(), jsonPath("articlesCount", is(1))},
        validArticle("articles[0]"));
  }

  public static ResultMatcher[] validSingleCommentModel() {
    return combine(
        new ResultMatcher[] {jsonPath("comment").isMap()}, validCommentModelInPath("comment"));
  }

  public static ResultMatcher[] validMultipleCommentModel() {
    return combine(
        new ResultMatcher[] {jsonPath("comments").isArray()},
        validCommentModelInPath("comments[0]"));
  }

  private static ResultMatcher[] validCommentModelInPath(String path) {
    return combine(
        new ResultMatcher[] {
          jsonPath(path + ".id").isNumber(),
          jsonPath(path + ".body").isString(),
          jsonPath(path + ".createdAt").isString(),
          jsonPath(path + ".updatedAt").isString()
        },
        validProfileInPath(path + ".author"));
  }

  private static ResultMatcher[] combine(ResultMatcher[] array1, ResultMatcher[] array2) {
    return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
        .toArray(
            size ->
                (ResultMatcher[]) Array.newInstance(array1.getClass().getComponentType(), size));
  }
}
