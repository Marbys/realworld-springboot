package io.github.marbys.myrealworldapp.infrastructure.jwt;

import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtUserService service;

  public JwtRequestFilter(JwtUserService service) {
    this.service = service;
  }

  @Override
  protected void doFilterInternal(
          HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Optional.ofNullable(request.getHeader("authorization"))
        .map(s -> new JwtEmailAuthenticationToken(s, service.payloadFromToken(s)))
        .ifPresent(SecurityContextHolder.getContext()::setAuthentication);
    filterChain.doFilter(request, response);
  }
}
