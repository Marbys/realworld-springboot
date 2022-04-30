package io.github.marbys.myrealworldapp.user;

import io.github.marbys.myrealworldapp.jwt.JwtUserPayload;
import io.github.marbys.myrealworldapp.user.User;

public interface UserService {
    UserModel authenticateUser(String email, String password);
    UserModel register(UserPostDTO user);
    UserModel updateUser(UserPutDTO userPutDTO, String token);
}
