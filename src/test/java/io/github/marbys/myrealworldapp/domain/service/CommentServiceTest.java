package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.TestUtil;
import io.github.marbys.myrealworldapp.application.controller.CommentControllerTest;
import io.github.marbys.myrealworldapp.application.dto.CommentPostDTO;
import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.marbys.myrealworldapp.TestUtil.*;
import static io.github.marbys.myrealworldapp.application.controller.CommentControllerTest.sampleComment;
import static io.github.marbys.myrealworldapp.application.controller.CommentControllerTest.sampleCommentPostDTO;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
  @Mock private UserFindService userFindService;
  @Mock private ArticleFindService articleFindService;
  @Mock private CommentRepository commentRepository;

  @InjectMocks private CommentService commentService;

  @Test
  void when_add_comment_expect_repository_save() {
    User author = sampleUser();
    Article article = sampleArticle();
    CommentPostDTO commentPostDTO = sampleCommentPostDTO();

    when(userFindService.findUserById(anyLong())).thenReturn(author);
    when(articleFindService.findBySlug(anyString())).thenReturn(article);
    commentService.addComment("slug", commentPostDTO, 1l);

    verify(commentRepository, times(1)).save(any(Comment.class));
  }

  @Test
  void when_delete_comment_expect_repository_delete() {
    Article article = sampleArticle();
    Comment comment = sampleComment();

    when(userFindService.findUserById(anyLong())).thenReturn(sampleUser());
    when(articleFindService.findBySlug(anyString())).thenReturn(article);
    when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
    article.getComments().add(comment);
    commentService.deleteComment("slug", 1l, 1l);

    verify(commentRepository, times(1)).deleteById(anyLong());
  }
}
