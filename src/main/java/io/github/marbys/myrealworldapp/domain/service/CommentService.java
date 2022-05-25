package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.application.dto.CommentPostDTO;
import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.domain.model.CommentModel;
import io.github.marbys.myrealworldapp.infrastructure.repository.ArticleRepository;
import io.github.marbys.myrealworldapp.infrastructure.repository.CommentRepository;
import io.github.marbys.myrealworldapp.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;

  public Comment addComment(String slug, CommentPostDTO commentPostDTO, long id) {
    User author = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    Article article = articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new);

    Comment comment = Comment.fromPostDTO(commentPostDTO);
    comment.setAuthor(author);
    comment.setArticle(article);

    return commentRepository.save(comment);
  }

  public void deleteComment(String slug, long commentId, long id) {
    User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    Comment comment =
        commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);
    if (!comment.getAuthor().equals(user)) throw new IllegalStateException();
    Article article = articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new);
    if (article.getComments().contains(comment)) commentRepository.deleteById(commentId);
  }

  public Set<Comment> getAllCommentsFromArticle(String slug) {
    return articleRepository
        .findBySlug(slug)
        .orElseThrow(NoSuchElementException::new)
        .getComments();
  }

  public Set<Comment> getAllCommentsFromArticle(String slug, long sub) {
    User user = userRepository.findById(sub).orElseThrow(NoSuchElementException::new);
    return getAllCommentsFromArticle(slug).stream()
        .map(user::withFollowingComment)
        .collect(Collectors.toSet());
  }
}
