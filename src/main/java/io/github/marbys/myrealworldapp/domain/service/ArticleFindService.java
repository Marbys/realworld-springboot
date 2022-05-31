package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.domain.Article;
import io.github.marbys.myrealworldapp.domain.User;

public interface ArticleFindService {

  public Article findBySlug(String slug);

  public Article findBySlug(String slug, User user);
}
