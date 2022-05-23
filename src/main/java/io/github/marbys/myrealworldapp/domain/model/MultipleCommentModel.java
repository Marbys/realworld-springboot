package io.github.marbys.myrealworldapp.domain.model;

import io.github.marbys.myrealworldapp.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.marbys.myrealworldapp.domain.model.CommentModel.CommentModelNested;

@Data
@AllArgsConstructor
public class MultipleCommentModel {
  private List<CommentModelNested> comments;

  public static MultipleCommentModel fromComments(Set<Comment> comments) {
    return new MultipleCommentModel(
        comments.stream()
            .map(CommentModel::fromComment)
            .map(CommentModelNested::fromCommentModel)
            .collect(Collectors.toList()));
  }
}