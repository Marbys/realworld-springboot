package io.github.marbys.myrealworldapp.article;

import io.github.marbys.myrealworldapp.comment.Comment;
import io.github.marbys.myrealworldapp.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "articles")
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String slug;

  @Embedded @NotNull private ArticleContent articleContent;

  @CreatedDate private Instant createdAt;
  @LastModifiedDate private Instant updatedAt;

  @JoinTable(
      name = "article_favourites",
      joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private Set<UserEntity> userFavorites = new HashSet<>();

  @Transient private boolean favorited = false;

  @OneToMany(
      mappedBy = "article",
      fetch = FetchType.EAGER,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  public Set<Comment> comments = new HashSet<>();

  @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
  @ManyToOne
  private UserEntity author;

  public Article(ArticleContent articleContent, UserEntity author) {
    this.articleContent = articleContent;
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Article article = (Article) o;
    return articleContent.equals(article.articleContent) && author.equals(article.author);
  }

  public int getFavoritesCount() {
    return userFavorites.size();
  }

  @Override
  public int hashCode() {
    return Objects.hash(articleContent, author);
  }

  @Override
  public String toString() {
    return "Article{"
        + "id="
        + id
        + ", slug='"
        + slug
        + '\''
        + ", articleContent="
        + articleContent
        + ", createdAt="
        + createdAt
        + ", updatedAt="
        + updatedAt
        + '}';
  }

  public Article setFavourite(boolean favorited) {
    this.favorited = favorited;
    return this;
  }

  @PrePersist
  @PreUpdate
  private void generateSlug() {
    this.slug = articleContent.getTitle().toLowerCase(Locale.ROOT).replaceAll(" ", "-");
  }
}
