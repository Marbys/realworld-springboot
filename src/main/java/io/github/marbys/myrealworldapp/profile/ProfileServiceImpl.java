package io.github.marbys.myrealworldapp.profile;

import io.github.marbys.myrealworldapp.user.UserRepository;

import java.util.Optional;

public class ProfileServiceImpl implements ProfileService{

    private final UserRepository repository;

    public ProfileServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Profile> viewProfile(String username) {

        return Optional.empty();
    }
}
