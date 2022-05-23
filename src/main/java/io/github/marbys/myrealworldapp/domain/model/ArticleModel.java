package io.github.marbys.myrealworldapp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.Profile;
import lombok.*;

import java.time.Instant;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.*;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ArticleModel {
  String slug;
  String title;
  String description;
  String body;

  @JsonProperty(namespace = "tagList")
  Set<String> tagList;

  Instant createdAt;
  Instant updatedAt;
  boolean favorited;
  int favoritesCount;
  Profile.ProfileModelNested author;

  public static ArticleModel fromArticle(Article article) {
    return new ArticleModel(
        article.getSlug(),
        article.getArticleContent().getTitle(),
        article.getArticleContent().getDescription(),
        article.getArticleContent().getBody(),
        TagModel.fromTagSet(article.getArticleContent().getTagList()),
        article.getCreatedAt(),
        article.getUpdatedAt(),
        article.isFavorited(),
        article.getFavoritesCount(),
        Profile.ProfileModelNested.fromProfile(article.getAuthor().getProfile()));
  }

  @Value
  @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  public static class ArticleModelNested {
    String slug;
    String title;
    String description;
    String body;

    @JsonProperty(namespace = "tagList")
    Set<String> tagList;

    Instant createdAt;
    Instant updatedAt;
    boolean favorited;
    int favoritesCount;
    Profile.ProfileModelNested author;

    public static ArticleModelNested fromArticle(Article article) {
      return new ArticleModelNested(
          article.getSlug(),
          article.getArticleContent().getTitle(),
          article.getArticleContent().getDescription(),
          article.getArticleContent().getBody(),
          TagModel.fromTagSet(article.getArticleContent().getTagList()),
          article.getCreatedAt(),
          article.getUpdatedAt(),
          article.isFavorited(),
          article.getFavoritesCount(),
          Profile.ProfileModelNested.fromProfile(article.getAuthor().getProfile()));
    }
  }
}
