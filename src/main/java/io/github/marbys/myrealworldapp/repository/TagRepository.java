package io.github.marbys.myrealworldapp.repository;

import io.github.marbys.myrealworldapp.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

  Optional<Tag> findFirstByNameEquals(String tag);
}
