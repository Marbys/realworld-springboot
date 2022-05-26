package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleFindService {
  private final ArticleRepository articleRepository;

  public Article findBySlug(String slug) {
    return articleRepository
        .findBySlug(slug)
        .orElseThrow(() -> new ApplicationException(ApplicationError.ARTICLE_NOT_FOUND));
  }

  public Article findBySlug(String slug, User user) {
    Article article = findBySlug(slug);
    article.setFavourite(article.getUserFavorites().contains(user));
    return article;
  }
}
