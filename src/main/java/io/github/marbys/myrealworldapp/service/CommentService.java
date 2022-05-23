package io.github.marbys.myrealworldapp.service;

import io.github.marbys.myrealworldapp.repository.CommentRepository;
import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.repository.ArticleRepository;
import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.domain.model.CommentModel;
import io.github.marbys.myrealworldapp.dto.CommentPostDTO;
import io.github.marbys.myrealworldapp.repository.UserRepository;
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

  public CommentModel addComment(String slug, CommentPostDTO commentPostDTO, long id) {
    User author = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    Article article = articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new);

    Comment comment = Comment.fromPostDTO(commentPostDTO);
    comment.setAuthor(author);
    comment.setArticle(article);

    commentRepository.save(comment);
    return CommentModel.fromComment(comment);
  }

  public void deleteComment(String slug, long commentId) {
    Comment comment =
        commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);
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
