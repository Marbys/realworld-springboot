package io.github.marbys.myrealworldapp.user;

import io.github.marbys.myrealworldapp.user.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByProfileUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    //boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
