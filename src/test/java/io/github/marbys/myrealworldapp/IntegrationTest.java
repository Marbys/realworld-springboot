package io.github.marbys.myrealworldapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.marbys.myrealworldapp.article.ArticleContent;
import io.github.marbys.myrealworldapp.article.ArticleModel;
import io.github.marbys.myrealworldapp.comment.CommentPostDTO;
import io.github.marbys.myrealworldapp.tag.Tag;
import io.github.marbys.myrealworldapp.user.UserLoginDTO;
import io.github.marbys.myrealworldapp.user.UserModel;
import io.github.marbys.myrealworldapp.user.UserPostDTO;
import io.github.marbys.myrealworldapp.user.UserPutDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static io.github.marbys.myrealworldapp.IntegrationTestUtils.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {
  private static final String USERNAME = "newuser";
  private static final String PASSWORD = "pwd";
  private static final String EMAIL = "newuser@gmail.com";

  private static final String FOLLOWED_USERNAME = "followed";
  private static final String FOLLOWED_PASSWORD = "followed";
  private static final String FOLLOWED_EMAIL = "followed@gmail.com";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private String token;
  private String followed_token;
  private String slug;

  @Order(1)
  @Test
  void auth_register() throws Exception {
    mockMvc
        .perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(new UserPostDTO(USERNAME, EMAIL, PASSWORD))))
        .andExpect(status().isCreated())
        .andExpectAll(validUserModel());
  }

  @Order(2)
  @Test
  void login_user() throws Exception {
    String user =
        mockMvc
            .perform(
                post("/api/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new UserLoginDTO(EMAIL, PASSWORD))))
            .andExpect(status().isOk())
            .andExpectAll(validUserModel())
            .andReturn()
            .getResponse()
            .getContentAsString();
    token = "Bearer " + objectMapper.readValue(user, UserModel.class).getToken();
  }

  @Order(3)
  @Test
  void update_user() throws Exception {
    mockMvc
        .perform(
            put("/api/user")
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(sampleUserPutDTO()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.bio").value("bio"))
        .andExpect(jsonPath("$.user.image").value("image"))
        .andExpectAll(validUserModel());
  }

  @Order(3)
  @Test
  void register_followed_user() throws Exception {
    String user =
        mockMvc
            .perform(
                post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            new UserPostDTO(FOLLOWED_USERNAME, FOLLOWED_EMAIL, FOLLOWED_PASSWORD))))
            .andExpect(status().isCreated())
            .andExpectAll(validUserModel())
            .andReturn()
            .getResponse()
            .getContentAsString();
    followed_token = "Bearer " + objectMapper.readValue(user, UserModel.class).getToken();
  }

  @Order(4)
  @Test
  void view_profile() throws Exception {
    mockMvc
        .perform(
            get("/api/profiles/{username}", USERNAME)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
        .andExpect(status().isOk())
        .andExpectAll(validProfile());
  }

  @Order(5)
  @Test
  void follow_user() throws Exception {
    mockMvc
        .perform(
            post("/api/profiles/{username}/follow", FOLLOWED_USERNAME)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validProfile())
        .andExpect(jsonPath("$.profile.following", is(true)));
  }

  @Order(6)
  @Test
  void unfollow_user() throws Exception {
    mockMvc
        .perform(
            delete("/api/profiles/{username}/follow", USERNAME)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Order(7)
  @Test
  void create_article() throws Exception {
    String article =
        mockMvc
            .perform(
                post("/api/articles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .content(objectMapper.writeValueAsString(sampleArticleContent())))
            .andExpect(status().isCreated())
            .andExpectAll(validSingleArticle())
            .andReturn()
            .getResponse()
            .getContentAsString();
    slug = objectMapper.readValue(article, ArticleModel.class).getSlug();
  }

  @Order(8)
  @Test
  void post_favorited_article() throws Exception {
    mockMvc
        .perform(
            post("/api/articles/{slug}/favorite", slug)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validSingleArticle())
        .andExpect(jsonPath("article.favorited", is(true)));
  }

  @Order(8)
  @Test
  void get_favorited_article() throws Exception {
    mockMvc
        .perform(
            get("/api/articles/{slug}", slug)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validSingleArticle())
        .andExpect(jsonPath("article.favorited", is(true)));
  }

  @Order(9)
  @Test
  void unfavorite_article() throws Exception {
    mockMvc
        .perform(
            delete("/api/articles/{slug}/favorite", slug)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validSingleArticle())
        .andExpect(jsonPath("article.favorited", is(false)));
  }

  @Order(9)
  @Test
  void put_article() throws Exception {
    ArticleContent articleToUpdate = sampleArticleContent();
    articleToUpdate.setDescription("New Description");

    mockMvc
        .perform(
            put("/api/articles/{slug}", slug)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleToUpdate)))
        .andExpect(status().isOk())
        .andExpectAll(validSingleArticle())
        .andExpect(jsonPath("$.article.description", is("New Description")));
  }

  @Order(10)
  @Test
  void post_comment() throws Exception {
    mockMvc
        .perform(
            post("/api/articles/{slug}/comments", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(sampleCommentPostDTO())))
        .andExpect(status().isCreated())
        .andExpectAll(validComment());
  }

  @Order(11)
  @Test
  void get_comments_from_article() throws Exception {
    mockMvc
        .perform(get("/api/articles/{slug}/comments", slug).accept(MediaType.APPLICATION_JSON))
        .andExpectAll(validMultipleCommentModel())
        .andExpect(status().isOk());
  }

  @Order(12)
  @Test
  void delete_comment() throws Exception {
    mockMvc
        .perform(
            delete("/api/articles/{slug}/comments/1", slug)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(sampleCommentPostDTO())))
        .andExpect(status().isNoContent());
  }

  @Order(13)
  @Test
  void get_multiple_articles() throws Exception {
    mockMvc
        .perform(get("/api/articles").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validMultipleArticleModel());
  }

  @Order(13)
  @Test
  void get_all_articles_with_tag() throws Exception {
    mockMvc
        .perform(get("/api/articles").queryParam("tag", "dragons").header(AUTHORIZATION, token))
        .andExpect(status().isOk())
        .andExpectAll(validMultipleArticleModel());
  }

  @Order(13)
  @Test
  void get_single_article_by_slug() throws Exception {
    mockMvc
        .perform(get("/api/articles/{slug}", slug))
        .andExpect(status().isOk())
        .andExpectAll(validSingleArticle());
  }

  @Order(14)
  @Test
  void delete_article() throws Exception {
    mockMvc
        .perform(delete("/api/articles/{slug}", slug).header("Authorization", token))
        .andExpect(status().isNoContent());
  }

  @Order(15)
  @Test
  void get_tags() throws Exception {
    mockMvc
        .perform(get("/api/tags").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("tags", is(hasSize(3))))
        .andExpect(jsonPath("tags[0]").isString());
  }

  public static CommentPostDTO sampleCommentPostDTO() {
    return new CommentPostDTO("His name was my name too.");
  }

  public static UserPutDTO sampleUserPutDTO() {
    return new UserPutDTO(EMAIL, USERNAME, PASSWORD, "bio", "image");
  }

  public static ArticleContent sampleArticleContent() {
    Set<Tag> tags = new HashSet<>();
    tags.add(new Tag("angularjs"));
    tags.add(new Tag("dragons"));
    tags.add(new Tag("reactjs"));
    return new ArticleContent(
        "Bad Birds in Quarantine",
        "Ever wonder how?",
        "Struggling to go legal in the underworld of finch smuggling.",
        tags);
  }
}
