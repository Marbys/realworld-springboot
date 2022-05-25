package io.github.marbys.myrealworldapp.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.marbys.myrealworldapp.application.dto.ArticlePostDTO;
import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.ArticleContent;
import io.github.marbys.myrealworldapp.domain.Tag;
import io.github.marbys.myrealworldapp.domain.service.ArticleService;
import io.github.marbys.myrealworldapp.infrastructure.configuration.WithMockJwtUser;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static io.github.marbys.myrealworldapp.IntegrationTestUtils.validMultipleArticleModel;
import static io.github.marbys.myrealworldapp.IntegrationTestUtils.validSingleArticleModel;
import static io.github.marbys.myrealworldapp.TestUtil.sampleArticle;
import static io.github.marbys.myrealworldapp.TestUtil.sampleTagSet;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

  @MockBean private ArticleService articleService;
  @MockBean private JwtUserService jwtUserService;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void when_get_article_then_return_sample_article() throws Exception {
    Article result = sampleArticle();
    String slug = "how-to-train-your-dragon";

    when(articleService.getArticleBySlug(slug)).thenReturn(result);

    mockMvc
        .perform(get("/api/articles/{slug}", slug).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validSingleArticleModel());
  }

  @Test
  @WithMockJwtUser
  void when_post_valid_article_return_article_model() throws Exception {
    ArticlePostDTO body = sampleArticlePostDto();

    when(articleService.createArticle(any(ArticleContent.class), anyLong()))
        .thenReturn(sampleArticle());

    mockMvc
        .perform(
            post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andExpectAll(validSingleArticleModel());
  }

  @Test
  void when_post_unauthenticated_return_status_forbidden() throws Exception {
    ArticlePostDTO body = sampleArticlePostDto();

    mockMvc
        .perform(
            post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockJwtUser
  void when_post_invalid_article_return_status_bad_request() throws Exception {
    ArticlePostDTO body =
        new ArticlePostDTO("", "desc", "body", Collections.singleton(new Tag("angular")));

    mockMvc
        .perform(
            post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockJwtUser
  void when_put_valid_article_return_article_model() throws Exception {
    ArticlePostDTO body = sampleArticlePostDto();
    String slug = "how-to-train-your-dragon";

    when(articleService.updateArticle(anyString(), any(ArticleContent.class), anyLong()))
        .thenReturn(sampleArticle());

    mockMvc
        .perform(
            put("/api/articles/{slug}", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpectAll(validSingleArticleModel());
  }

  @Test
  void when_put_unauthenticated_return_status_forbidden() throws Exception {
    ArticlePostDTO body = sampleArticlePostDto();
    String slug = "how-to-train-your-dragon";

    mockMvc
        .perform(
            put("/api/articles/{slug}", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockJwtUser
  void when_delete_article_return_status_no_content() throws Exception {
    String slug = "how-to-train-your-dragon";

    mockMvc.perform(delete("/api/articles/{slug}", slug)).andExpect(status().isNoContent());
  }

  @Test
  void when_delete_unauthenticated_return_status_forbidden() throws Exception {
    String slug = "how-to-train-your-dragon";

    mockMvc.perform(delete("/api/articles/{slug}", slug)).andExpect(status().isForbidden());
  }

  @Test
  @WithMockJwtUser
  void when_favorite_article_then_return_favorited_article_model() throws Exception {
    Article article = sampleArticle();
    article.setFavorited(true);
    String slug = "how-to-train-your-dragon";
    when(articleService.favoriteArticle(eq(slug), anyLong())).thenReturn(article);

    mockMvc
        .perform(post("/api/articles/{slug}/favorite", slug))
        .andExpect(status().isOk())
        .andExpectAll(validSingleArticleModel())
        .andExpect(jsonPath("$.article.favorited", is(true)));
  }

  @Test
  @WithMockJwtUser
  void when_unfavorite_article_then_return_unfavorited_article_model() throws Exception {
    Article body = sampleArticle();
    body.setFavorited(false);
    String slug = "how-to-train-your-dragon";
    when(articleService.unfavoriteArticle(eq(slug), anyLong())).thenReturn(body);

    mockMvc
        .perform(delete("/api/articles/{slug}/favorite", slug))
        .andExpect(status().isOk())
        .andExpectAll(validSingleArticleModel())
        .andExpect(jsonPath("$.article.favorited", is(false)));
  }

  @Test
  @WithMockJwtUser
  void when_get_all_valid_article_return_multiple_articles_model() throws Exception {
    ArticlePostDTO body = sampleArticlePostDto();

    when(articleService.findAll(any(), any(), anyLong()))
        .thenReturn(Collections.singletonList(sampleArticle()));

    mockMvc
        .perform(
            get("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpectAll(validMultipleArticleModel())
        .andExpect(jsonPath("articlesCount", is(1)));
  }

  @Test
  @WithMockJwtUser
  void when_get_feed_then_return_valid_multiple_model_articles() throws Exception {
    Article article = sampleArticle();

    when(articleService.getFeed(anyLong(), any())).thenReturn(Collections.singletonList(article));

    mockMvc
        .perform(
            get("/api/articles/feed")
                .accept(MediaType.APPLICATION_JSON)
                .param("limit", "1")
                .param("offset", "0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("articlesCount", is(1)))
        .andExpectAll(validMultipleArticleModel());
  }

  public static ArticlePostDTO sampleArticlePostDto() {
    return new ArticlePostDTO(
        "How to train your dragon", "Ever wonder how?", "It takes a Jacobian", sampleTagSet());
  }
}
