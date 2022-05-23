package io.github.marbys.myrealworldapp.user;

import io.github.marbys.myrealworldapp.jwt.JwtPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users/login")
  public ResponseEntity<UserModel> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
    return ResponseEntity.of(Optional.of(userService.login(userLoginDTO)));
  }

  @PostMapping("/users")
  public ResponseEntity<UserModel> register(@RequestBody @Valid UserPostDTO user) {
    return ResponseEntity.status(CREATED).body(userService.register(user));
  }

  @PutMapping("/user")
  public ResponseEntity<?> updateUser(
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
