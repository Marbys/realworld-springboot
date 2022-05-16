package io.github.marbys.myrealworldapp.comment;

import io.github.marbys.myrealworldapp.jwt.JwtPayload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class CommentController {

    private CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping("/{slug}/comments")
    public Optional<Comment> addComment(@PathVariable String slug, @RequestBody Comment comment, @AuthenticationPrincipal JwtPayload jwtPayload) {
        return Optional.of(service.addComment(slug, comment, jwtPayload.getSub()));
    }

    @DeleteMapping("/{slug}/comments/{commentId}")
    public void deleteComment(@PathVariable String slug, @PathVariable long commentId) {
        service.deleteComment(slug, commentId);
    }
}
