package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface CommentFindService {
  Comment findByCommentId(long id);
}
