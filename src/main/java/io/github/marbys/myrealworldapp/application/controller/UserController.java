package io.github.marbys.myrealworldapp.application.controller;

import io.github.marbys.myrealworldapp.application.dto.UserLoginDTO;
import io.github.marbys.myrealworldapp.application.dto.UserPostDTO;
import io.github.marbys.myrealworldapp.application.dto.UserPutDTO;
import io.github.marbys.myrealworldapp.domain.Profile;
import io.github.marbys.myrealworldapp.domain.model.UserModel;
import io.github.marbys.myrealworldapp.domain.service.UserService;
import io.github.marbys.myrealworldapp.infrastructure.jwt.JwtPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/users/login")
  public ResponseEntity<UserModel> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
    return ResponseEntity.of(Optional.of(userService.login(userLoginDTO)));
  }

  @PostMapping("/users")
  public ResponseEntity<UserModel> register(@RequestBody @Valid UserPostDTO user) {
    return ResponseEntity.status(CREATED).body(userService.register(user));
  }

  @GetMapping("/user")
  public ResponseEntity<UserModel> getUser(@AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.ok(userService.findUser(jwtPayload.getSub()));
  }

  @PutMapping("/user")
  public ResponseEntity<UserModel> updateUser(
      @RequestBody UserPutDTO userPutDTO, @AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.ok(userService.updateUser(userPutDTO, jwtPayload.getSub()));
  }

  @GetMapping("/profiles/{username}")
  public ResponseEntity<Profile> viewProfile(
      @PathVariable String username, @AuthenticationPrincipal JwtPayload payload) {
    return payload != null
        ? ResponseEntity.ok(userService.viewProfile(username, payload.getSub()))
        : ResponseEntity.ok(userService.viewProfile(username));
  }

  @PostMapping("/profiles/{username}/follow")
  public ResponseEntity<?> follow(
      @PathVariable String username, @AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.status(OK).body(userService.followUser(username, jwtPayload.getSub()));
  }

  @DeleteMapping("/profiles/{username}/follow")
  public ResponseEntity<?> unfollow(
      @PathVariable String username, @AuthenticationPrincipal JwtPayload jwtPayload) {
    return ResponseEntity.ok(userService.unfollowUser(username, jwtPayload.getSub()));
  }
}
