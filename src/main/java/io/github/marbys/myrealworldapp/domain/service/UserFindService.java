package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFindService {
  private final UserRepository userRepository;

  public User findUserById(long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));
  }

  public User findUserByUsername(String username) {
    return userRepository
        .findByProfileUsername(username)
        .orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));
  }
}
