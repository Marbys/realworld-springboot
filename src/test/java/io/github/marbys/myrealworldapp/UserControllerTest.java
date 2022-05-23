package io.github.marbys.myrealworldapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.marbys.myrealworldapp.controller.UserController;
import io.github.marbys.myrealworldapp.dto.UserLoginDTO;
import io.github.marbys.myrealworldapp.dto.UserPostDTO;
import io.github.marbys.myrealworldapp.dto.UserPutDTO;
import io.github.marbys.myrealworldapp.infrastructure.jwt.HmacSha256Service;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtUserService;
import io.github.marbys.myrealworldapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.marbys.myrealworldapp.IntegrationTestUtils.validProfile;
import static io.github.marbys.myrealworldapp.IntegrationTestUtils.validUserModel;
import static io.github.marbys.myrealworldapp.TestUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private UserService userService;
  @MockBean private HmacSha256Service hmacSha256Service;
  @MockBean private JwtUserService jwtUserService;

  @BeforeEach
  public void returnJwtToken() {
    when(jwtUserService.tokenFromUserEntity(any())).thenReturn("MOCKED_TOKEN");
  }

  @Test
  void when_login_user_expect_valid_userModel() throws Exception {
    when(userService.login(new UserLoginDTO("user@gmail.com", "password")))
        .thenReturn(sampleUserModel());
    mockMvc
        .perform(
            post("/api/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleLoginDTO())))
        .andExpect(status().isOk())
        .andExpectAll(validUserModel());
  }

  @Test
  void when_login_invalid_user_expect_status_bad_request() throws Exception {
    mockMvc
        .perform(
            post("/api/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserLoginDTO("", ""))))
        .andExpect(status().isBadRequest());
  }

  @Test
  void when_login_user_with_invalid_authorization_expect_ignore_token() throws Exception {
    when(userService.login(sampleLoginDTO())).thenReturn(sampleUserModel());
    mockMvc
        .perform(
            post("/api/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleLoginDTO()))
                .header(AUTHORIZATION, "invalid auth"))
        .andExpect(status().isOk())
        .andExpectAll(validUserModel());
  }

  @Test
  void when_post_user_expect_valid_userModel() throws Exception {
    when(userService.register(any(UserPostDTO.class))).thenReturn(sampleUserModel());
    mockMvc
        .perform(
            post("/api/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePostDTO())))
        .andExpect(status().isCreated())
        .andExpectAll(validUserModel());
  }

  @Test
  void when_post_invalid_user_expect_status_bad_request() throws Exception {
    when(userService.register(any(UserPostDTO.class))).thenReturn(sampleUserModel());
    mockMvc
        .perform(
            post("/api/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserPostDTO("", "", ""))))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockJwtUser
  void when_put_user_expect_valid_userModel() throws Exception {
    when(userService.updateUser(any(UserPutDTO.class), anyLong())).thenReturn(sampleUserModel());
    mockMvc
        .perform(
            put("/api/user")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePutDTO())))
        .andExpect(status().isOk())
        .andExpectAll(validUserModel());
  }

  @Test
  void when_put_user_unauthenticated_expect_valid_userModel() throws Exception {
    mockMvc.perform(put("/api/user")).andExpect(status().isForbidden());
  }

  @Test
  void when_get_profile_expect_valid_profile() throws Exception {
    when(userService.viewProfile(anyString())).thenReturn(sampleProfile());
    mockMvc
        .perform(get("/api/profiles/user").accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validProfile());
  }

  @Test
  @WithMockJwtUser
  void when_follow_user_expect_valid_profile() throws Exception {
    when(userService.followUser(anyString(), anyLong())).thenReturn(sampleProfile());
    mockMvc
        .perform(post("/api/profiles/user/follow").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validProfile());
  }

  @Test
  void when_follow_profile_unauthenticated_expect_status_forbidden() throws Exception {
    mockMvc.perform(post("/api/profiles/user/follow")).andExpect(status().isForbidden());
  }

  @Test
  @WithMockJwtUser
  void when_unfollow_profile_expect_valid_user_model() throws Exception {
    when(userService.unfollowUser(anyString(), anyLong())).thenReturn(sampleProfile());
    mockMvc
        .perform(delete("/api/profiles/user/follow").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validProfile());
  }

  @Test
  void when_unfollow_profile_unauthenticated_expect_status_forbidden() throws Exception {
    mockMvc.perform(delete("/api/profiles/user/follow")).andExpect(status().isForbidden());
  }
}
