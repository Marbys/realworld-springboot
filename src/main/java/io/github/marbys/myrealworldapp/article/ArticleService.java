package io.github.marbys.myrealworldapp.article;

import io.github.marbys.myrealworldapp.user.UserEntity;
import io.github.marbys.myrealworldapp.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ArticleService {

    private UserRepository userRepository;
    private ArticleRepository articleRepository;

    public ArticleService(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public ArticleContent getArticleBySlug(String slug) {
        return articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new).getArticleContent();
    }

    public ArticleContent createArticle(ArticleContent articleContent, long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        Article article = new Article(articleContent, user);
        articleRepository.save(article);
        return articleContent;
    }

    public ArticleContent updateArticle(String slug, ArticleContent articleContent, long id) {
        Article originalArticle = getOriginalArticle(slug, id);

        originalArticle.getArticleContent().updateArticle(articleContent);
        articleRepository.save(originalArticle);
        return articleContent;
    }

    public void deleteArticle(String slug, long id) {
        Article originalArticle = getOriginalArticle(slug, id);
        articleRepository.delete(originalArticle);
    }

    private Article getOriginalArticle(String slug, long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        Article originalArticle = articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new);

        if (!originalArticle.getAuthor().equals(user))
            throw new IllegalStateException();
        return originalArticle;
    }

}
