package io.github.marbys.myrealworldapp.application.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.marbys.myrealworldapp.domain.Tag;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@JsonTypeName("article")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class ArticlePostDTO {

  @NotBlank String title;
  @NotBlank String description;
  @NotBlank String body;

  Set<Tag> tagList;
}
