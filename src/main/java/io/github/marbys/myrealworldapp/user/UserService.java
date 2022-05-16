package io.github.marbys.myrealworldapp.user;

import io.github.marbys.myrealworldapp.jwt.JwtUserService;
import io.github.marbys.myrealworldapp.profile.Profile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository repository;
    private JwtUserService service;
    private UserMapper mapper;
    private PasswordEncoder encoder;

    public UserService(UserRepository repository, JwtUserService service, UserMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.service = service;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    public UserModel login(UserLoginDTO userLoginDTO) {
        return repository.findByEmail(userLoginDTO.getEmail())
                .filter(entity -> entity.matchesPassword(userLoginDTO.getPassword(), encoder))
                .map(entity -> UserModel.fromEntityAndToken(entity, service.tokenFromUserEntity(entity)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

    }

    public UserModel register(UserPostDTO user) {
        if (repository.existsByEmail(user.getEmail()))
            throw new IllegalStateException("User with this email already exists!");

        UserEntity entity = UserEntity.from(user.getUsername(), user.getPassword(), user.getEmail());
        entity.setPassword(encoder.encode(entity.getPassword()));
        entity = repository.save(entity);

        String token = service.tokenFromUserEntity(entity);
        return UserModel.fromEntityAndToken(entity, token);
    }

    public UserModel updateUser(UserPutDTO userPutDTO, Long id) {
        UserEntity userEntity = repository.findById(id).get();
        repository.save(updateUser(userPutDTO, userEntity));

        String newToken = service.tokenFromUserEntity(userEntity);
        return UserModel.fromEntityAndToken(userEntity, newToken);
    }

    public Profile viewProfile(String username, Long id) {
        Profile profile = repository.findByProfileUsername(username)
                .map(UserEntity::getProfile)
                .orElseThrow(NoSuchElementException::new);

        if (id > 0) {
            boolean match = repository.findById(id)
                    .get()
                    .getFollowingUsers()
                    .stream()
                    .anyMatch(entity -> entity.getProfile().getUsername().equals(username));

            profile.setFollowing(match);
        }
        return profile;
    }

    public Profile followUser(String username, Long id) {
        UserEntity followee = repository.findByProfileUsername(username).orElseThrow(NoSuchElementException::new);
        repository.findById(id)
                .map(e -> e.follow(followee))
                .map(e -> repository.save(e))
                .map(UserEntity::getProfile)
                .orElseThrow(NoSuchElementException::new);
        return viewProfile(username, id);
    }

    public Profile unfollowUser(String username, Long id) {
        UserEntity followee = repository.findByProfileUsername(username).orElseThrow(NoSuchElementException::new);
        repository.findById(id)
                .map(e -> e.unfollow(followee))
                .map(e -> repository.save(e))
                .map(UserEntity::getProfile)
                .orElseThrow(NoSuchElementException::new);
        return viewProfile(username, id);
    }


    private UserEntity updateUser(UserPutDTO userPutDTO, UserEntity userEntity) {
        userPutDTO.getEmailToUpdate().ifPresent(userEntity::setEmail);
        userPutDTO.getUsernameToUpdate().ifPresent(userEntity.getProfile()::setUsername);
        userPutDTO.getPasswordToUpdate().ifPresent(s -> userEntity.setPassword(encoder.encode(s)));
        userPutDTO.getBioToUpdate().ifPresent(userEntity.getProfile()::setBio);
        userPutDTO.getImageToUpdate().ifPresent(userEntity.getProfile()::setImage);
        return userEntity;
    }
}
