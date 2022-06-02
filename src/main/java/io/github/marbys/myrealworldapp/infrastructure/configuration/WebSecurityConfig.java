package io.github.marbys.myrealworldapp.infrastructure.configuration;

import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtAuthenticationProvider;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtRequestFilter jwtRequestFilter;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable();
    httpSecurity.headers().frameOptions().disable();
    httpSecurity
        .authorizeRequests()
        .antMatchers("/users/login", "/users", "/h2-console/**")
        .permitAll()
        .antMatchers(GET, "/articles/**")
        .permitAll()
        .antMatchers(GET, "/articles/feed")
        .authenticated()
        .antMatchers(GET, "/articles")
        .permitAll()
        .antMatchers(GET, "/profiles/**")
        .permitAll()
        .antMatchers(GET, "/tags")
        .permitAll()
        .anyRequest()
        .authenticated();
    httpSecurity.cors().and();
    httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    configuration.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "DELETE", "PUT"));
    configuration.setAllowCredentials(true);
    configuration.addAllowedHeader("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider() {
    return new JwtAuthenticationProvider();
  }
}
