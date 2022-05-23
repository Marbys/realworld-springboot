package io.github.marbys.myrealworldapp.repository;

import io.github.marbys.myrealworldapp.domain.Comment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Modifying
  @Query("delete from Comment c where c.id = ?1")
  void deleteById(@NonNull Long id);
}
