package io.github.marbys.myrealworldapp.comment;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@JsonTypeName("comment")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentPostDTO {
  @NotBlank public String body;
}
