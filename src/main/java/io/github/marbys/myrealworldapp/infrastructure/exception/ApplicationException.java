package io.github.marbys.myrealworldapp.infrastructure.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

  private final ApplicationError error;

  public ApplicationException(ApplicationError error) {
    super(error.getMessage());
    this.error = error;
  }
}
