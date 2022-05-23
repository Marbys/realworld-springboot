package io.github.marbys.myrealworldapp.article;

import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class MultipleArticlesModel {

  List<ArticleModel.ArticleModelNested> articles;
  int articlesCount;

  public static MultipleArticlesModel fromArticles(List<Article> articles) {
    return new MultipleArticlesModel(
        articles.stream()
            .map(ArticleModel.ArticleModelNested::fromArticle)
            .collect(Collectors.toList()),
        articles.size());
  }
}
