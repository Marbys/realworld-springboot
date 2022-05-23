package io.github.marbys.myrealworldapp.infrastructure.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class JwtEmailAuthenticationToken extends AbstractAuthenticationToken {

  private final String token;
  private final JwtPayload jwtPayload;

  public JwtEmailAuthenticationToken(String token, JwtPayload jwtPayload) {
    super(Collections.singletonList(new SimpleGrantedAuthority("USER")));
    super.setAuthenticated(true);
    this.token = token;
    this.jwtPayload = jwtPayload;
  }

  @Override
  public Object getPrincipal() {
    return jwtPayload;
  }

  @Override
  public Object getCredentials() {
    return token;
  }
}
