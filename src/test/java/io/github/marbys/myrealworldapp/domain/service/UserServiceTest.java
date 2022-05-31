package io.github.marbys.myrealworldapp.domain.service;

import io.github.marbys.myrealworldapp.application.dto.UserLoginDTO;
import io.github.marbys.myrealworldapp.application.dto.UserPostDTO;
import io.github.marbys.myrealworldapp.application.dto.UserPutDTO;
import io.github.marbys.myrealworldapp.domain.Profile;
import io.github.marbys.myrealworldapp.domain.User;
import io.github.marbys.myrealworldapp.domain.model.UserModel;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationError;
import io.github.marbys.myrealworldapp.infrastructure.exception.ApplicationException;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtUserService;
import io.github.marbys.myrealworldapp.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static io.github.marbys.myrealworldapp.TestUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private JwtUserService jwtUserService;
  @Mock private PasswordEncoder encoder;

  @InjectMocks private UserService userService;

  @Test
  public void when_valid_register_user_expect_valid_user_model() {
    UserPostDTO user = samplePostDTO();

    when(jwtUserService.tokenFromUserEntity(any())).thenReturn("token");
    when(encoder.encode((anyString()))).thenReturn("password");
    when(userRepository.save(any())).thenReturn(sampleUser());

    UserModel register = userService.register(user);
    assertThat(register.getUsername()).isEqualTo(user.getUsername());
    verify(userRepository, times(1)).save(any());
  }

  @Test
  public void when_register_duplicate_user_expect_exception() {
    UserPostDTO user = samplePostDTO();

    when(userRepository.existsByEmail(anyString())).thenReturn(true);

    ApplicationException exception =
        assertThrows(ApplicationException.class, () -> userService.register(user));
    assertThat(ApplicationError.DUPLICATED_USER).isEqualTo(exception.getError());
  }

  @Test
  public void when_login_valid_user_expect_valid_user_model() {
    UserLoginDTO user = sampleLoginDTO();

    when(jwtUserService.tokenFromUserEntity(any())).thenReturn("token");
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(sampleUser()));
    when(sampleUser().matchesPassword(user.getPassword(), encoder)).thenReturn(true);

    UserModel login = userService.login(user);
    assertThat(login.getToken()).isEqualTo("token");
  }

  @Test
  public void when_find_existing_user_expect_valid_user_model() {
    String username = sampleUser().getProfile().getUsername();

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser()));

    assertThat(username).isEqualTo(userService.findUser(1).getUsername());
  }

  @Test
  public void when_update_user_expect_updated_user_model() {
    UserPutDTO userPutDTO = new UserPutDTO("new@gmail.com", "user", "pwd", "bio", "");

    when(encoder.encode((any()))).thenReturn("password");
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser()));

    UserModel userModel = userService.updateUser(userPutDTO, 1l);
    assertThat(userPutDTO.getEmail()).isEqualTo(userModel.getEmail());
    verify(userRepository, times(1)).save(any());
  }

  @Test
  public void when_view_profile_return_valid_profile() {
    String username = sampleUser().getProfile().getUsername();

    when(userRepository.findByProfileUsername(username)).thenReturn(Optional.of(sampleUser()));

    Profile profile = userService.viewProfile(username);
    assertThat(username).isEqualTo(profile.getUsername());
  }

  @Test
  public void when_follow_user_return_followee_user() {
    User followee = sampleUser();
    User follower = sampleUser();

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(follower));
    when(userRepository.findByProfileUsername(anyString())).thenReturn(Optional.of(followee));
    when(userRepository.save(followee)).thenReturn(followee);

    userService.followUser("followee", 1l);
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  public void when_unfollow_user_return_unfollowed_user() {
    User followee = sampleUser();
    User follower = sampleUser();

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(follower));
    when(userRepository.findByProfileUsername(anyString())).thenReturn(Optional.of(followee));
    when(userRepository.save(followee)).thenReturn(followee);

    userService.unfollowUser("followee", 1l);
    verify(userRepository, times(1)).save(any(User.class));
  }
}
