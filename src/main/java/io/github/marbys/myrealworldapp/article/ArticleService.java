package io.github.marbys.myrealworldapp.article;

import io.github.marbys.myrealworldapp.jwt.PageRequest;
import io.github.marbys.myrealworldapp.tag.Tag;
import io.github.marbys.myrealworldapp.tag.TagService;
import io.github.marbys.myrealworldapp.user.UserEntity;
import io.github.marbys.myrealworldapp.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleService {

  private UserRepository userRepository;
  private ArticleRepository articleRepository;
  private TagService tagService;

  public ArticleService(
      UserRepository userRepository, ArticleRepository articleRepository, TagService tagService) {
    this.userRepository = userRepository;
    this.articleRepository = articleRepository;
    this.tagService = tagService;
  }

  public Article getArticleBySlug(String slug) {
    return articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new);
  }

  public Article createArticle(ArticleContent articleContent, long id) {
    UserEntity user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
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
    UserEntity user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    Article originalArticle =
        articleRepository
            .findBySlug(slug)
            .map(article -> article.setFavourite(article.getUserFavorites().contains(user)))
            .orElseThrow(NoSuchElementException::new);

    if (!originalArticle.getAuthor().equals(user)) throw new IllegalStateException();
    return originalArticle;
  }

  public Article favoriteArticle(String slug, long sub) {
    UserEntity user = userRepository.findById(sub).orElseThrow(NoSuchElementException::new);
    Article article = articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new);
    article.getUserFavorites().add(user);
    articleRepository.save(article);
    return article.setFavourite(true);
  }

  public Article unfavoriteArticle(String slug, long sub) {
    UserEntity user = userRepository.findById(sub).orElseThrow(NoSuchElementException::new);
    Article article = articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new);
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

  public List<Article> findAll(Map<String, String> params, Pageable pageable, long sub) {
    UserEntity userEntity = userRepository.findById(sub).orElseThrow(NoSuchElementException::new);
    return findAll(params, pageable).stream()
        .peek(
            article -> {
              if (article.getUserFavorites().contains(userEntity)) article.setFavourite(true);
              userEntity.withFollowingArticle(article);
            })
        .collect(Collectors.toList());
  }
}
