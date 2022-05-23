package io.github.marbys.myrealworldapp.repository;

import io.github.marbys.myrealworldapp.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByProfileUsername(String username);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}
