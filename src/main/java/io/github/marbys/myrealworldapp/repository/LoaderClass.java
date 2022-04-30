package io.github.marbys.myrealworldapp.repository;

import io.github.marbys.myrealworldapp.user.UserEntity;
import io.github.marbys.myrealworldapp.user.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoaderClass implements ApplicationRunner {

    private PasswordEncoder encoder;
    private UserRepository repository;

    public LoaderClass(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        repository.save(new UserEntity("user", encoder.encode("user"), "user@gmail.com"));

    }
}
