package io.github.marbys.myrealworldapp;

import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtUserService;
import io.github.marbys.myrealworldapp.domain.model.UserModel;
import io.github.marbys.myrealworldapp.repository.UserRepository;
import io.github.marbys.myrealworldapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private JwtUserService jwtUserService;
  @Mock private PasswordEncoder encoder;
  @InjectMocks private UserService userService;

  @BeforeEach
  public void init() {
    when(jwtUserService.tokenFromUserEntity(any())).thenReturn("token");
  }

  @Test
  public void register_user() {
    when(encoder.encode((any()))).thenReturn("password");
    when(userRepository.save(any())).thenReturn(sampleUser());
    UserModel register = userService.register(samplePostDTO());
    assertEquals(register.getUsername(), samplePostDTO().getUsername());
    verify(userRepository, times(1)).save(any());
  }

  @Test
  public void login_user() {
    when(userRepository.findByEmail(sampleLoginDTO().getEmail()))
        .thenReturn(Optional.of(sampleUser()));
    when(sampleUser().matchesPassword(sampleLoginDTO().getPassword(), encoder)).thenReturn(true);
    UserModel login = userService.login(sampleLoginDTO());
    assertThat(login.getToken()).isEqualTo("token");
  }
}
