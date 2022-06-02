package io.github.marbys.myrealworldapp.application.controller;

import io.github.marbys.myrealworldapp.domain.Tag;
import io.github.marbys.myrealworldapp.domain.service.TagService;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.github.marbys.myrealworldapp.TestUtil.sampleTagSet;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
public class TagControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TagService tagService;
  @MockBean private JwtUserService jwtUserService;

  @Test
  void when_get_tags_return_tags() throws Exception {
    Set<Tag> result = sampleTagSet();

    when(tagService.getAllTags()).thenReturn(result);

    mockMvc
        .perform(get("/tags").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpectAll(validMultipleTagModel());
  }

  private ResultMatcher[] validMultipleTagModel() {
    return new ResultMatcher[] {
      jsonPath("tags[0]").isString(), jsonPath("tags[1]").isString(),
    };
  }
}
