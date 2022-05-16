package io.github.marbys.myrealworldapp.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findByAuthor_Id(long id);
    Optional<Article> findBySlug(String slug);
}
