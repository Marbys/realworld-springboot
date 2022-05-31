package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.ArticleContent;
import io.github.marbys.myrealworldapp.domain.Tag;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.jwt.PageRequest;
import io.github.marbys.myrealworldapp.infrastructure.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService implements ArticleFindService {

  private final UserFindService userFindService;
  private final ArticleRepository articleRepository;
  private final TagService tagService;

  public Article getArticleBySlug(String slug) {
    return findBySlug(slug);
  }

  public Article createArticle(ArticleContent articleContent, long id) {
    User user = userFindService.findUserById(id);
    Article article = new Article(articleContent, user);
    Set<Tag> tags = tagService.findTags(articleContent.getTagList());
    article.getArticleContent().setTagList(tags);
    return articleRepository.save(article);
  }

  public Article updateArticle(String slug, ArticleContent articleContent, long id) {
    Article originalArticle = getArticleBySlug(slug, id);
    originalArticle.getArticleContent().updateArticle(articleContent);
    return articleRepository.save(originalArticle);
  }

  public void deleteArticle(String slug, long id) {
    Article originalArticle = getArticleBySlug(slug, id);
    articleRepository.delete(originalArticle);
  }

  public Article getArticleBySlug(String slug, long id) {
    User user = userFindService.findUserById(id);
    return findBySlug(slug, user);
  }

  public Article favoriteArticle(String slug, long id) {
    User user = userFindService.findUserById(id);
    Article article = findBySlug(slug);
    article.getUserFavorites().add(user);
    articleRepository.save(article);
    return article.setFavourite(true);
  }

  public Article unfavoriteArticle(String slug, long id) {
    User user = userFindService.findUserById(id);
    Article article = findBySlug(slug);
    article.getUserFavorites().remove(user);
    articleRepository.save(article);
    return article.setFavourite(false);
  }

  public List<Article> findAll(Map<String, String> params, Pageable pageable) {
    String author = params.get("author");
    String tag = params.get("tag");
    String favorited = params.get("favorited");
    int offset = params.get("offset") != null ? Integer.parseInt(params.get("offset")) : 0;
    int limit = params.get("limit") != null ? Integer.parseInt(params.get("limit")) : 20;

    Pageable page =
        PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
            .withPage(pageable.getPageNumber());
    return articleRepository.findByFilters(tag, author, favorited, page).getContent();
  }

  public List<Article> findAll(Map<String, String> params, Pageable pageable, long id) {
    User user = userFindService.findUserById(id);
    return findAll(params, pageable).stream()
        .peek(
            article -> {
              if (article.getUserFavorites().contains(user)) article.setFavourite(true);
              user.withFollowingArticle(article);
            })
        .collect(Collectors.toList());
  }

  public List<Article> getFeed(long id, Pageable pageable) {
    User user = userFindService.findUserById(id);
    Pageable page =
        PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"))
            .withPage(pageable.getPageNumber());
    return articleRepository.findByFavorited(user, page).getContent().stream()
        .peek(
            article -> {
              if (article.getUserFavorites().contains(user)) article.setFavourite(true);
              user.withFollowingArticle(article);
            })
        .collect(Collectors.toList());
  }

  @Override
  public Article findBySlug(String slug) {
    return articleRepository
        .findBySlug(slug)
        .orElseThrow(() -> new ApplicationException(ApplicationError.ARTICLE_NOT_FOUND));
  }

  @Override
  public Article findBySlug(String slug, User user) {
    Article article = findBySlug(slug);
    article.setFavourite(article.getUserFavorites().contains(user));
    return article;
  }
}
