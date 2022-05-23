package io.github.marbys.myrealworldapp.controller;

import io.github.marbys.myrealworldapp.service.CommentService;
import io.github.marbys.myrealworldapp.domain.model.CommentModel;
import io.github.marbys.myrealworldapp.domain.model.MultipleCommentModel;
import io.github.marbys.myrealworldapp.dto.CommentPostDTO;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtPayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class CommentController {

  private CommentService service;

  public CommentController(CommentService service) {
    this.service = service;
  }

  @PostMapping("/{slug}/comments")
  public ResponseEntity<CommentModel> addComment(
      @PathVariable String slug,
      @RequestBody CommentPostDTO comment,
      @AuthenticationPrincipal JwtPayload jwtPayload) {
    return new ResponseEntity<>(
        service.addComment(slug, comment, jwtPayload.getSub()), HttpStatus.CREATED);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{slug}/comments/{commentId}")
  public void deleteComment(@PathVariable String slug, @PathVariable long commentId) {
    service.deleteComment(slug, commentId);
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
