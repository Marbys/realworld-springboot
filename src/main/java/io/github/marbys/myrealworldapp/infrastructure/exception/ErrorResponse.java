package io.github.marbys.myrealworldapp.infrastructure.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
  private final List<String> body;

  public ErrorResponse() {
    body = new ArrayList<>();
  }

  public void append(String message) {
    body.add(message);
  }
}
