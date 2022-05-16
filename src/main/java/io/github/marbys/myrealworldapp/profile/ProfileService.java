package io.github.marbys.myrealworldapp.profile;

import java.util.Optional;

public interface ProfileService {

    Optional<Profile> viewProfile(String username);
}
