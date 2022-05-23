package io.github.marbys.myrealworldapp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.domain.Profile;
import lombok.*;

import java.time.Instant;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonTypeName("comment")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class CommentModel {
  long id;
  Instant createdAt;
  Instant updatedAt;
  String body;

  @JsonProperty(namespace = "author")
  Profile.ProfileModelNested author;

  public static CommentModel fromComment(Comment comment) {
    return new CommentModel(
        comment.getId(),
        comment.getCreatedAt(),
        comment.getUpdatedAt(),
        comment.getBody(),
        Profile.ProfileModelNested.fromProfile(comment.getAuthor().getProfile()));
  }

  @Value
  @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  public static class CommentModelNested {
    long id;
    Instant createdAt;
    Instant updatedAt;
    String body;

    @JsonProperty(namespace = "author")
    Profile.ProfileModelNested author;

    public static CommentModelNested fromCommentModel(CommentModel comment) {
      return new CommentModelNested(
          comment.getId(),
          comment.getCreatedAt(),
          comment.getUpdatedAt(),
          comment.getBody(),
          comment.getAuthor());
    }
  }
}
