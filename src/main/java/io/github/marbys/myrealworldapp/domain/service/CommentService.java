package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.application.dto.CommentPostDTO;
import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.repository.ArticleRepository;
import io.github.marbys.myrealworldapp.infrastructure.repository.CommentRepository;
import io.github.marbys.myrealworldapp.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;

  public Comment addComment(String slug, CommentPostDTO commentPostDTO, long id) {
    User author =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));
    Article article =
        articleRepository
            .findBySlug(slug)
            .orElseThrow(() -> new ApplicationException(ApplicationError.ARTICLE_NOT_FOUND));

    Comment comment = Comment.fromPostDTO(commentPostDTO);
    comment.setAuthor(author);
    comment.setArticle(article);

    return commentRepository.save(comment);
  }

  public void deleteComment(String slug, long commentId, long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));
    Comment comment =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new ApplicationException(ApplicationError.COMMENT_NOT_FOUND));
    if (!comment.getAuthor().equals(user))
      throw new ApplicationException(ApplicationError.INVALID_REQUEST_COMMENT);
    Article article =
        articleRepository
            .findBySlug(slug)
            .orElseThrow(() -> new ApplicationException(ApplicationError.ARTICLE_NOT_FOUND));

    if (article.getComments().contains(comment)) commentRepository.deleteById(commentId);
  }

  public Set<Comment> getAllCommentsFromArticle(String slug) {
    return articleRepository
        .findBySlug(slug)
        .orElseThrow(() -> new ApplicationException(ApplicationError.ARTICLE_NOT_FOUND))
        .getComments();
  }

  public Set<Comment> getAllCommentsFromArticle(String slug, long sub) {
    User user =
        userRepository
            .findById(sub)
            .orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));
    return getAllCommentsFromArticle(slug).stream()
        .map(user::withFollowingComment)
        .collect(Collectors.toSet());
  }
}
