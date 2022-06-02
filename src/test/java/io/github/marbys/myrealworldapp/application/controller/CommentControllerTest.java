package io.github.marbys.myrealworldapp.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.marbys.myrealworldapp.IntegrationTestUtils;
import io.github.marbys.myrealworldapp.TestUtil;
import io.github.marbys.myrealworldapp.application.dto.CommentPostDTO;
import io.github.marbys.myrealworldapp.domain.Comment;
import io.github.marbys.myrealworldapp.domain.service.CommentService;
import io.github.marbys.myrealworldapp.infrastructure.configuration.WithMockJwtUser;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;

import static io.github.marbys.myrealworldapp.IntegrationTestUtils.validMultipleCommentModel;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

  @MockBean private CommentService commentService;
  @MockBean private JwtUserService jwtUserService;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockJwtUser
  void when_post_valid_comment_return_valid_comment_model() throws Exception {
    CommentPostDTO body = sampleCommentPostDTO();
    String slug = "how-to-train-your-dragon";

    when(commentService.addComment(anyString(), any(CommentPostDTO.class), anyLong()))
        .thenReturn(sampleComment());

    mockMvc
        .perform(
            post("/articles/{slug}/comments", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andExpectAll(IntegrationTestUtils.validComment());
  }

  @Test
  void when_post_comment_unauthenticated_return_status_forbidden() throws Exception {
    CommentPostDTO body = sampleCommentPostDTO();
    String slug = "how-to-train-your-dragon";

    mockMvc
        .perform(
            post("/articles/{slug}/comments", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockJwtUser
  void when_delete_valid_comment_return_status_no_content() throws Exception {
    String slug = "how-to-train-your-dragon";

    mockMvc
        .perform(delete("/articles/{slug}/comments/{commentId}", slug, 1))
        .andExpect(status().isNoContent());
  }

  @Test
  void when_delete_unauthenticated_valid_comment_return_status_forbidden() throws Exception {
    String slug = "how-to-train-your-dragon";

    mockMvc
        .perform(delete("/articles/{slug}/comments/{commentId}", slug, 1))
        .andExpect(status().isForbidden());
  }

  @Test
  void when_get_comments_return_multiple_comment_model() throws Exception {
    String slug = "how-to-train-your-dragon";

    when(commentService.getAllCommentsFromArticle(anyString()))
        .thenReturn(Collections.singleton(sampleComment()));

    mockMvc
        .perform(get("/articles/{slug}/comments", slug).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validMultipleCommentModel());
  }

  public static Comment sampleComment() {
    Comment comment = new Comment("nice article");
    comment.setId(1l);
    comment.setAuthor(TestUtil.sampleUser());
    comment.setCreatedAt(Instant.now());
    comment.setUpdatedAt(Instant.now());
    return comment;
  }

  public static CommentPostDTO sampleCommentPostDTO() {
    return new CommentPostDTO("nice article");
  }
}
