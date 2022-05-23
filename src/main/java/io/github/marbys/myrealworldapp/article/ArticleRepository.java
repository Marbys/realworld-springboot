package io.github.marbys.myrealworldapp.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {

  Article findByAuthor_Id(long id);

  Optional<Article> findBySlug(String slug);

  @Query(
      "SELECT DISTINCT a FROM Article a "
          + "LEFT JOIN a.articleContent.tagList t "
          + "LEFT JOIN a.author p "
          + "LEFT JOIN a.userFavorites f "
          + "WHERE "
          + "(:tag IS NULL OR t.name IN :tag) AND "
          + "(:author IS NULL OR p.profile.username = :author) AND "
          + "(:favorited IS NULL OR f.profile.username = :favorited)")
  Page<Article> findByFilters(
      @Nullable String tag, @Nullable String author, @Nullable String favorited, Pageable pageable);
}
