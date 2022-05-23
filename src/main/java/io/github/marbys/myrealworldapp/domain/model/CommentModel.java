package io.github.marbys.myrealworldapp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("comment")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class CommentModel {
  private long id;
  private Instant createdAt;
  private Instant updatedAt;
  private String body;

  @JsonProperty(namespace = "author")
  private Profile.ProfileModelNested author;

  public static CommentModel fromComment(Comment comment) {
    return new CommentModel(
        comment.getId(),
        comment.getCreatedAt(),
        comment.getUpdatedAt(),
        comment.getBody(),
        Profile.ProfileModelNested.fromProfile(comment.getAuthor().getProfile()));
  }

  @Data
  @AllArgsConstructor
  public static class CommentModelNested {
    private long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String body;

    @JsonProperty(namespace = "author")
    private Profile.ProfileModelNested author;

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
