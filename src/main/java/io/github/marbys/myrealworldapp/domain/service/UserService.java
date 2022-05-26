package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.domain.Profile;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.domain.model.UserModel;
import io.github.marbys.myrealworldapp.application.dto.UserLoginDTO;
import io.github.marbys.myrealworldapp.application.dto.UserPostDTO;
import io.github.marbys.myrealworldapp.application.dto.UserPutDTO;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtUserService;
import io.github.marbys.myrealworldapp.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final UserFindService userFindService;
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
      throw new ApplicationException(ApplicationError.DUPLICATED_USER);
    User entity = User.from(user.getUsername(), user.getPassword(), user.getEmail());
    entity.setPassword(encoder.encode(entity.getPassword()));
    entity = repository.save(entity);
    String token = service.tokenFromUserEntity(entity);
    return UserModel.fromEntityAndToken(entity, token);
  }

  public UserModel findUser(long id) {
    return UserModel.fromEntity(userFindService.findUserById(id));
  }

  public UserModel updateUser(UserPutDTO userPutDTO, Long id) {
    User user = userFindService.findUserById(id);
    repository.save(updateUser(userPutDTO, user));
    String newToken = service.tokenFromUserEntity(user);
    return UserModel.fromEntityAndToken(user, newToken);
  }

  public Profile viewProfile(String username, Long id) {
    Profile profile = userFindService.findUserByUsername(username).getProfile();
    if (id > 0) {
      boolean match =
          repository
              .findById(id)
              .orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND))
              .getFollowingUsers()
              .stream()
              .anyMatch(entity -> entity.getProfile().getUsername().equals(username));
      profile.setFollowing(match);
    }
    return profile;
  }

  public Profile viewProfile(String username) {
    return userFindService.findUserByUsername(username).getProfile();
  }

  public Profile followUser(String username, Long id) {
    User followee = userFindService.findUserByUsername(username);
    repository
        .findById(id)
        .map(e -> e.follow(followee))
        .map(repository::save)
        .map(User::getProfile)
        .orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));
    return viewProfile(username, id);
  }

  public Profile unfollowUser(String username, Long id) {
    User followee = userFindService.findUserByUsername(username);
    repository
        .findById(id)
        .map(e -> e.unfollow(followee))
        .map(repository::save)
        .map(User::getProfile)
        .orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));
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
