package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.domain.Tag;
import io.github.marbys.myrealworldapp.infrastructure.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository repository;

  public Set<Tag> getAllTags() {
    return new HashSet<>(repository.findAll());
  }

  public Set<Tag> findTags(Set<Tag> tags) {
    return tags.stream()
        .map(tag -> repository.findFirstByNameEquals(tag.getName()).orElse(tag))
        .collect(Collectors.toSet());
  }
}
