package io.github.marbys.myrealworldapp.tag;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

  private final TagRepository repository;

  public TagService(TagRepository repository) {
    this.repository = repository;
  }

  public Set<Tag> getAllTags() {
    return new HashSet<>(repository.findAll());
  }

  public Set<Tag> findTags(Set<Tag> tags) {
    return tags.stream()
        .map(tag -> repository.findFirstByNameEquals(tag.getName()).orElse(tag))
        .collect(Collectors.toSet());
  }
}
