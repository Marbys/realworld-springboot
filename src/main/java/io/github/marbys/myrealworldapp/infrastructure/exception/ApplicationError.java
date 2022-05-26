package io.github.marbys.myrealworldapp.infrastructure.exception;

import lombok.Getter;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Getter
public enum ApplicationError {
  DUPLICATED_USER("User with this credentials already exists.", HttpStatus.UNPROCESSABLE_ENTITY),

  USER_NOT_FOUND("User not found.", HttpStatus.NOT_FOUND),
  ARTICLE_NOT_FOUND("Article not found.", HttpStatus.NOT_FOUND),
  COMMENT_NOT_FOUND("Comment not found.", HttpStatus.NOT_FOUND),

  INVALID_REQUEST_COMMENT("Only the author can modify his comment.", HttpStatus.BAD_REQUEST);

  private final String message;
  private final HttpStatus status;

  ApplicationError(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}
