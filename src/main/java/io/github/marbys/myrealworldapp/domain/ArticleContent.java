package io.github.marbys.myrealworldapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.marbys.myrealworldapp.application.dto.ArticlePostDTO;
import io.github.marbys.myrealworldapp.application.dto.ArticlePutDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleContent {

  @NotBlank private String title;
  @NotBlank private String description;
  @NotBlank private String body;

  @JoinTable(
      name = "article_tags",
      joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
  @ManyToMany(cascade = CascadeType.PERSIST)
  @JsonProperty("tagList")
  private Set<Tag> tagList = new HashSet<>();

  public void updateArticle(ArticleContent articleContent) {
    title = articleContent.getTitle() == null ? title : articleContent.getTitle();
    description =
        articleContent.getDescription() == null ? description : articleContent.getDescription();
    body = articleContent.getBody() == null ? body : articleContent.getBody();
  }

  public static ArticleContent from(ArticlePostDTO articlePostDto) {
    return new ArticleContent(
        articlePostDto.getTitle(),
        articlePostDto.getDescription(),
        articlePostDto.getBody(),
        articlePostDto.getTagList());
  }

  public static ArticleContent from(ArticlePutDTO articlePutDTO) {
    return new ArticleContent(
        articlePutDTO.getTitle(),
        articlePutDTO.getDescription(),
        articlePutDTO.getBody(),
        articlePutDTO.getTagList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArticleContent that = (ArticleContent) o;
    return title.equals(that.title)
        && description.equals(that.description)
        && body.equals(that.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, body);
  }
}
