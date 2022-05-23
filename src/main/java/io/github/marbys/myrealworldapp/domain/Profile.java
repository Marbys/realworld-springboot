package io.github.marbys.myrealworldapp.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@JsonTypeName("profile")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
public class Profile {
  @Column(name = "name")
  @NonNull
  private String username;

  private String bio;
  private String image;

  @Transient private boolean following;

  public Profile withFollowing(boolean following) {
    this.following = following;
    return this;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProfileModelNested {
    String username;
    String bio;
    String image;
    boolean following;

    public static ProfileModelNested fromProfile(Profile profile) {
      return new ProfileModelNested(
          profile.getUsername(), profile.getBio(), profile.getImage(), profile.isFollowing());
    }
  }
}
