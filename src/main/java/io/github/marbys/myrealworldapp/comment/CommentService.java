package io.github.marbys.myrealworldapp.comment;

import io.github.marbys.myrealworldapp.article.Article;
import io.github.marbys.myrealworldapp.article.ArticleRepository;
import io.github.marbys.myrealworldapp.user.UserEntity;
import io.github.marbys.myrealworldapp.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public Comment addComment(String slug, Comment comment, long id) {
        UserEntity author = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        Article article = articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new);

        comment.setAuthor(author);
        comment.setArticle(article);

        commentRepository.save(comment);
        return comment;
    }

    public void deleteComment(String slug, long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);
        Article article = articleRepository.findBySlug(slug).orElseThrow(NoSuchElementException::new);

        if (article.getComments().contains(comment))
            commentRepository.deleteById(commentId);
    }
}
