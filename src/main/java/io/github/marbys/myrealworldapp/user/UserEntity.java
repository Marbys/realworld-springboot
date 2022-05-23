package io.github.marbys.myrealworldapp.user;

import io.github.marbys.myrealworldapp.article.Article;
import io.github.marbys.myrealworldapp.comment.Comment;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.REMOVE;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull private String password;

  @NonNull
  @Column(unique = true)
  private String email;

  @Embedded @NonNull private Profile profile;

  @JoinTable(
      name = "user_followings",
      joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "followee_id", referencedColumnName = "id"))
  @OneToMany(cascade = REMOVE)
  private Set<UserEntity> followingUsers = new HashSet<>();

  public UserEntity follow(UserEntity follower) {
    followingUsers.add(follower);
    return this;
  }

  public UserEntity unfollow(UserEntity follower) {
    followingUsers.remove(follower);
    return this;
  }

  public boolean matchesPassword(String rawPassword, PasswordEncoder encoder) {
    return encoder.matches(rawPassword, password);
  }

  public static UserEntity from(String username, String password, String email) {
    return new UserEntity(password, email, new Profile(username));
  }

  public Article withFollowingArticle(Article article) {
    withFollowing(article.getAuthor());
    return article;
  }

  public Comment withFollowingComment(Comment comment) {
    withFollowing(comment.getAuthor());
    return comment;
  }

  public Profile withFollowing(UserEntity entity) {
    return entity.getProfile().withFollowing(followingUsers.contains(entity));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserEntity user = (UserEntity) o;
    return email.equals(user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
}
