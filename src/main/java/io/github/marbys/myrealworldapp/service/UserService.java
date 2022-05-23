package io.github.marbys.myrealworldapp.service;

import io.github.marbys.myrealworldapp.domain.Profile;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.domain.model.UserModel;
import io.github.marbys.myrealworldapp.dto.UserLoginDTO;
import io.github.marbys.myrealworldapp.dto.UserPostDTO;
import io.github.marbys.myrealworldapp.dto.UserPutDTO;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtUserService;
import io.github.marbys.myrealworldapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final JwtUserService service;
  private final PasswordEncoder encoder;

  public UserModel login(UserLoginDTO userLoginDTO) {
    return repository
        .findByEmail(userLoginDTO.getEmail())
        .filter(entity -> entity.matchesPassword(userLoginDTO.getPassword(), encoder))
        .map(entity -> UserModel.fromEntityAndToken(entity, service.tokenFromUserEntity(entity)))
        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
  }

  public UserModel register(UserPostDTO user) {
    if (repository.existsByEmail(user.getEmail()))
      throw new IllegalStateException("User with this email already exists!");
    User entity = User.from(user.getUsername(), user.getPassword(), user.getEmail());
    entity.setPassword(encoder.encode(entity.getPassword()));
    entity = repository.save(entity);
    String token = service.tokenFromUserEntity(entity);
    return UserModel.fromEntityAndToken(entity, token);
  }

  public UserModel updateUser(UserPutDTO userPutDTO, Long id) {
    User userEntity = repository.findById(id).orElseThrow(NoSuchElementException::new);
    repository.save(updateUser(userPutDTO, userEntity));
    String newToken = service.tokenFromUserEntity(userEntity);
    return UserModel.fromEntityAndToken(userEntity, newToken);
  }

  public Profile viewProfile(String username, Long id) {
    Profile profile =
        repository
            .findByProfileUsername(username)
            .map(User::getProfile)
            .orElseThrow(NoSuchElementException::new);
    if (id > 0) {
      boolean match =
          repository
              .findById(id)
              .orElseThrow(NoSuchElementException::new)
              .getFollowingUsers()
              .stream()
              .anyMatch(entity -> entity.getProfile().getUsername().equals(username));
      profile.setFollowing(match);
    }
    return profile;
  }

  public Profile viewProfile(String username) {
    return repository
        .findByProfileUsername(username)
        .map(User::getProfile)
        .orElseThrow(NoSuchElementException::new);
  }

  public Profile followUser(String username, Long id) {
    User followee =
        repository.findByProfileUsername(username).orElseThrow(NoSuchElementException::new);
    repository
        .findById(id)
        .map(e -> e.follow(followee))
        .map(e -> repository.save(e))
        .map(User::getProfile)
        .orElseThrow(NoSuchElementException::new);
    return viewProfile(username, id);
  }

  public Profile unfollowUser(String username, Long id) {
    User followee =
        repository.findByProfileUsername(username).orElseThrow(NoSuchElementException::new);
    repository
        .findById(id)
        .map(e -> e.unfollow(followee))
        .map(e -> repository.save(e))
        .map(User::getProfile)
        .orElseThrow(NoSuchElementException::new);
    return viewProfile(username, id);
  }

  private User updateUser(UserPutDTO userPutDTO, User userEntity) {
    userPutDTO.getEmailToUpdate().ifPresent(userEntity::setEmail);
    userPutDTO.getUsernameToUpdate().ifPresent(userEntity.getProfile()::setUsername);
    userPutDTO.getPasswordToUpdate().ifPresent(s -> userEntity.setPassword(encoder.encode(s)));
    userPutDTO.getBioToUpdate().ifPresent(userEntity.getProfile()::setBio);
    userPutDTO.getImageToUpdate().ifPresent(userEntity.getProfile()::setImage);
    return userEntity;
  }
}
