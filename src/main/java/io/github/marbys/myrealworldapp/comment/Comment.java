package io.github.marbys.myrealworldapp.comment;

import io.github.marbys.myrealworldapp.article.Article;
import io.github.marbys.myrealworldapp.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
@RequiredArgsConstructor
@NoArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate private Instant createdAt;
  @LastModifiedDate private Instant updatedAt;
  @NonNull private String body;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
  private UserEntity author;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false)
  private Article article;

  public static Comment fromPostDTO(CommentPostDTO commentPostDTO) {
    return new Comment(commentPostDTO.getBody());
  }

  @Override
  public String toString() {
    return "Comment{"
        + "id="
        + id
        + ", createdAt="
        + createdAt
        + ", updatedAt="
        + updatedAt
        + ", body='"
        + body
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Comment comment = (Comment) o;
    return Objects.equals(createdAt, comment.createdAt)
        && Objects.equals(body, comment.body)
        && Objects.equals(author, comment.author)
        && Objects.equals(article, comment.article);
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdAt, body, author, article);
  }
}
