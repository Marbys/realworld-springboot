package io.github.marbys.myrealworldapp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.Set;

@JsonTypeName("article")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleModel {
  private String slug;
  private String title;
  private String description;
  private String body;

  @JsonProperty(namespace = "tagList")
  private Set<String> tagList;

  private Instant createdAt;
  private Instant updatedAt;
  private boolean favorited;
  private int favoritesCount;
  private Profile.ProfileModelNested author;

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

  @AllArgsConstructor
  @Value
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
