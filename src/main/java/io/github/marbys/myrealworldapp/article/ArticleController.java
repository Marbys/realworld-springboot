package io.github.marbys.myrealworldapp.article;

import io.github.marbys.myrealworldapp.jwt.JwtPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ArticleController {
    @Autowired
    private ArticleService service;

    @GetMapping("/articles/{slug}")
    public Optional<ArticleContent> getArticle(@PathVariable String slug) {
        return Optional.of(service.getArticleBySlug(slug));
    }

    @PostMapping("/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<ArticleContent> createArticle(@RequestBody ArticleContent articleContent, @AuthenticationPrincipal JwtPayload jwtPayload) {
        return Optional.of(service.createArticle(articleContent, jwtPayload.getSub()));
    }

    @PutMapping("/articles/{slug}")
    public Optional<ArticleContent> updateArticle(@PathVariable String slug,@RequestBody ArticleContent articleContent, @AuthenticationPrincipal JwtPayload jwtPayload) {
        return Optional.of(service.updateArticle(slug, articleContent, jwtPayload.getSub()));
    }

    @DeleteMapping("/articles/{slug}")
    public void deleteArticle(@PathVariable String slug, @AuthenticationPrincipal JwtPayload jwtPayload) {
        service.deleteArticle(slug, jwtPayload.getSub());
    }
}
