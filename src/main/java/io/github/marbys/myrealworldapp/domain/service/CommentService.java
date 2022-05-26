package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.application.dto.CommentPostDTO;
import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentFindService commentFindService;
  private final UserFindService userFindService;
  private final ArticleFindService articleFindService;
  private final CommentRepository commentRepository;

  public Comment addComment(String slug, CommentPostDTO commentPostDTO, long id) {
    User author = userFindService.findUserById(id);
    Article article = articleFindService.findBySlug(slug);

    Comment comment = Comment.fromPostDTO(commentPostDTO);
    comment.setAuthor(author);
    comment.setArticle(article);

    return commentRepository.save(comment);
  }

  public void deleteComment(String slug, long commentId, long id) {
    User author = userFindService.findUserById(id);
    Comment comment = commentFindService.findByCommentId(commentId);

    if (!comment.getAuthor().equals(author))
      throw new ApplicationException(ApplicationError.INVALID_REQUEST_COMMENT);
    Article article = articleFindService.findBySlug(slug);

    if (article.getComments().contains(comment)) commentRepository.deleteById(commentId);
  }

  public Set<Comment> getAllCommentsFromArticle(String slug) {
    return articleFindService.findBySlug(slug).getComments();
  }

  public Set<Comment> getAllCommentsFromArticle(String slug, long id) {
    User author = userFindService.findUserById(id);
    return getAllCommentsFromArticle(slug).stream()
        .map(author::withFollowingComment)
        .collect(Collectors.toSet());
  }
}
