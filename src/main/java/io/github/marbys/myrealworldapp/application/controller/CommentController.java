package io.github.marbys.myrealworldapp.application.controller;

import io.github.marbys.myrealworldapp.domain.model.CommentModel;
import io.github.marbys.myrealworldapp.domain.model.MultipleCommentModel;
import io.github.marbys.myrealworldapp.application.dto.CommentPostDTO;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtPayload;
import io.github.marbys.myrealworldapp.domain.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService service;

  @PostMapping("/{slug}/comments")
  public ResponseEntity<CommentModel> addComment(
      @PathVariable String slug,
      @RequestBody CommentPostDTO comment,
      @AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.status(CREATED)
        .body(CommentModel.fromComment(service.addComment(slug, comment, jwtPayload.getSub())));
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{slug}/comments/{commentId}")
  public void deleteComment(
      @PathVariable String slug,
      @AuthenticationPrincipal JwtPayload jwtPayload,
      @PathVariable long commentId) {
    service.deleteComment(slug, commentId, jwtPayload.getSub());
  }

  @GetMapping("/{slug}/comments")
  public ResponseEntity<MultipleCommentModel> getCommentsFromArticle(
      @PathVariable String slug, @AuthenticationPrincipal JwtPayload jwtPayload) {
    if (jwtPayload != null)
      return ResponseEntity.ok(
          MultipleCommentModel.fromComments(
              service.getAllCommentsFromArticle(slug, jwtPayload.getSub())));
    return ResponseEntity.ok(
        MultipleCommentModel.fromComments(service.getAllCommentsFromArticle(slug)));
  }
}
