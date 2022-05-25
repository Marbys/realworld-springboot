package io.github.marbys.myrealworldapp.infrastructure.repository;

import io.github.marbys.myrealworldapp.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByProfileUsername(String username);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}
