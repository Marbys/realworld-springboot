package io.github.marbys.myrealworldapp.user;

import java.util.Optional;

public interface AuthenticationService {
    UserModel authenticateUser(String email, String password);
}
