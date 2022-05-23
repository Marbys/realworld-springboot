package io.github.marbys.myrealworldapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    title = articleContent.getTitle().isEmpty() ? title : articleContent.getTitle();
    description =
        articleContent.getDescription().isEmpty() ? description : articleContent.getDescription();
    body = articleContent.getBody().isEmpty() ? body : articleContent.getBody();
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
