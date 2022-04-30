package io.github.marbys.myrealworldapp.user;

import io.github.marbys.myrealworldapp.jwt.JwtUserPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    public UserController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping(value = "/users/login")
    public ResponseEntity<?> authenticate(@RequestBody UserLoginDTO user) {
        UserModel userModel = userService.authenticateUser(user.getEmail(), user.getPassword());
        return ResponseEntity.of(Optional.ofNullable(userModel));
    }

    @PostMapping("/users")
    public ResponseEntity<?> register(@RequestBody @Valid UserPostDTO user) {
        UserModel register = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(register);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserPutDTO userPutDTO,  @RequestHeader (name="Authorization") String token) {
        return ResponseEntity.of(Optional.ofNullable(userService.updateUser(userPutDTO, token)));
    }
}
