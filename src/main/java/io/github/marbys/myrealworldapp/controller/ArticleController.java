package io.github.marbys.myrealworldapp.controller;

import io.github.marbys.myrealworldapp.domain.ArticleContent;
import io.github.marbys.myrealworldapp.domain.model.ArticleModel;
import io.github.marbys.myrealworldapp.domain.model.MultipleArticleModel;
import io.github.marbys.myrealworldapp.dto.ArticlePostDto;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtPayload;
import io.github.marbys.myrealworldapp.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.github.marbys.myrealworldapp.domain.model.ArticleModel.fromArticle;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService service;

  @GetMapping("/articles/{slug}")
  public ResponseEntity<ArticleModel> getArticle(
      @PathVariable String slug, @AuthenticationPrincipal JwtPayload jwtPayload) {
    if (jwtPayload != null)
      return ResponseEntity.ok(fromArticle(service.getArticleBySlug(slug, jwtPayload.getSub())));
    return ResponseEntity.ok(fromArticle(service.getArticleBySlug(slug)));
  }

  @PostMapping("/articles")
  public ResponseEntity<ArticleModel> createArticle(
      @RequestBody ArticlePostDto articlePostDto, @AuthenticationPrincipal JwtPayload jwtPayload) {
    System.out.println(articlePostDto.toString());
    return ResponseEntity.status(CREATED)
        .body(
            fromArticle(
                service.createArticle(ArticleContent.from(articlePostDto), jwtPayload.getSub())));
  }

  @PutMapping("/articles/{slug}")
  public ResponseEntity<ArticleModel> updateArticle(
      @PathVariable String slug,
      @RequestBody ArticlePostDto articlePostDto,
      @AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.ok(
        fromArticle(
            service.updateArticle(slug, ArticleContent.from(articlePostDto), jwtPayload.getSub())));
  }

  @DeleteMapping("/articles/{slug}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteArticle(
      @PathVariable String slug, @AuthenticationPrincipal JwtPayload jwtPayload) {
    service.deleteArticle(slug, jwtPayload.getSub());
  }

  @PostMapping("/articles/{slug}/favorite")
  public ResponseEntity<ArticleModel> favoriteArticle(
      @PathVariable String slug, @AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.ok(
        ArticleModel.fromArticle(service.favoriteArticle(slug, jwtPayload.getSub())));
  }

  @DeleteMapping("/articles/{slug}/favorite")
  public ResponseEntity<ArticleModel> unfavoriteArticle(
      @PathVariable String slug, @AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.ok(
        ArticleModel.fromArticle(service.unfavoriteArticle(slug, jwtPayload.getSub())));
  }

  @GetMapping("/articles")
  public ResponseEntity<MultipleArticleModel> articles(
      @RequestParam Map<String, String> params,
      Pageable pageable,
      @AuthenticationPrincipal JwtPayload jwtPayload) {
    if (jwtPayload != null)
      return ResponseEntity.ok(
          MultipleArticleModel.fromArticles(
              service.findAll(params, pageable, jwtPayload.getSub())));
    return ResponseEntity.ok(MultipleArticleModel.fromArticles(service.findAll(params, pageable)));
  }

  @GetMapping("/articles/feed")
  public ResponseEntity<MultipleArticleModel> articleFeed(
      @AuthenticationPrincipal JwtPayload jwtPayload, Pageable pageable) {
    return ResponseEntity.ok(
        MultipleArticleModel.fromArticles(service.getFeed(jwtPayload.getSub(), pageable)));
  }
}
