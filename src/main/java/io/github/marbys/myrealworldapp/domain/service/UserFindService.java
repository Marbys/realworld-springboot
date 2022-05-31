package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.domain.User;

public interface UserFindService {

  User findUserById(long id);

  User findUserByUsername(String username);
}
