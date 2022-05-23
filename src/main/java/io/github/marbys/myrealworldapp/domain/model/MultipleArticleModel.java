package io.github.marbys.myrealworldapp.domain.model;

import io.github.marbys.myrealworldapp.domain.Article;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MultipleArticleModel {

  List<ArticleModel.ArticleModelNested> articles;
  int articlesCount;

  public static MultipleArticleModel fromArticles(List<Article> articles) {
    return new MultipleArticleModel(
        articles.stream()
            .map(ArticleModel.ArticleModelNested::fromArticle)
            .collect(Collectors.toList()),
        articles.size());
  }
}
