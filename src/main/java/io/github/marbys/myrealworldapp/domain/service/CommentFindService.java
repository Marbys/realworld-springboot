package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentFindService {
  private final CommentRepository commentRepository;

  public Comment findByCommentId(long id) {
    return commentRepository
        .findById(id)
        .orElseThrow(() -> new ApplicationException(ApplicationError.COMMENT_NOT_FOUND));
  }
}
