package io.github.marbys.myrealworldapp.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

public class JwtAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    return Optional.of(authentication)
        .map(EmailAuthenticationToken.class::cast)
        .orElseThrow(IllegalStateException::new);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return EmailAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
