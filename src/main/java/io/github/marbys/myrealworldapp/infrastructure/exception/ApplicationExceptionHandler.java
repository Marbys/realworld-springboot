package io.github.marbys.myrealworldapp.infrastructure.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<String> handleApplicationException(
      ApplicationException applicationException) {
    return ResponseEntity.status(applicationException.getError().getStatus())
        .body(applicationException.getError().getMessage());
  }
}
