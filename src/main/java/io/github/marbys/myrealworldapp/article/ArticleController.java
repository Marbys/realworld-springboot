package io.github.marbys.myrealworldapp.article;

import io.github.marbys.myrealworldapp.jwt.JwtPayload;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.github.marbys.myrealworldapp.article.ArticleModel.fromArticle;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api")
public class ArticleController {
  private ArticleService service;

  public ArticleController(ArticleService service) {
    this.service = service;
  }

  @GetMapping("/articles/{slug}")
  public ResponseEntity<ArticleModel> getArticle(
      @PathVariable String slug, @AuthenticationPrincipal JwtPayload jwtPayload) {
    if (jwtPayload != null)
      return ResponseEntity.ok(fromArticle(service.getArticleBySlug(slug, jwtPayload.getSub())));
    return ResponseEntity.ok(fromArticle(service.getArticleBySlug(slug)));
  }

  @PostMapping("/articles")
  public ResponseEntity<ArticleModel> createArticle(
      @RequestBody ArticleContent articleContent, @AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.status(CREATED)
        .body(fromArticle(service.createArticle(articleContent, jwtPayload.getSub())));
  }

  @PutMapping("/articles/{slug}")
  public ResponseEntity<ArticleModel> updateArticle(
      @PathVariable String slug,
      @RequestBody ArticleContent articleContent,
      @AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.ok(
        fromArticle(service.updateArticle(slug, articleContent, jwtPayload.getSub())));
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
  public ResponseEntity<MultipleArticlesModel> articles(
      @RequestParam Map<String, String> params,
      Pageable pageable,
      @AuthenticationPrincipal JwtPayload jwtPayload) {
    if (jwtPayload != null)
      return ResponseEntity.ok(
          MultipleArticlesModel.fromArticles(
              service.findAll(params, pageable, jwtPayload.getSub())));
    return ResponseEntity.ok(MultipleArticlesModel.fromArticles(service.findAll(params, pageable)));
  }

  @GetMapping("/articles/feed")
  public ResponseEntity<MultipleArticlesModel> articleFeed(
      @AuthenticationPrincipal JwtPayload jwtPayload) {
    return null;
  }
}
