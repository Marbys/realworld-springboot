package io.github.marbys.myrealworldapp.domain.model;

import io.github.marbys.myrealworldapp.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Data
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
