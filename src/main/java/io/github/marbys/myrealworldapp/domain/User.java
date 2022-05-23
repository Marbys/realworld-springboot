package io.github.marbys.myrealworldapp.domain;

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
public class User {

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
  private Set<User> followingUsers = new HashSet<>();

  public User follow(User follower) {
    followingUsers.add(follower);
    return this;
  }

  public User unfollow(User follower) {
    followingUsers.remove(follower);
    return this;
  }

  public boolean matchesPassword(String rawPassword, PasswordEncoder encoder) {
    return encoder.matches(rawPassword, password);
  }

  public static User from(String username, String password, String email) {
    return new User(password, email, new Profile(username));
  }

  public Article withFollowingArticle(Article article) {
    withFollowing(article.getAuthor());
    return article;
  }

  public Comment withFollowingComment(Comment comment) {
    withFollowing(comment.getAuthor());
    return comment;
  }

  public Profile withFollowing(User entity) {
    return entity.getProfile().withFollowing(followingUsers.contains(entity));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return email.equals(user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
}
