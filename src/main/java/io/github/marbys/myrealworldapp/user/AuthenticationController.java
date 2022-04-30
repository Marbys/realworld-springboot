package io.github.marbys.myrealworldapp.user;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.marbys.myrealworldapp.jwt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

//    @PostMapping(value = "/login")
//    public ResponseEntity<?> authenticate(@RequestBody UserLoginDTO user) {
//        UserModel userModel = authenticationService.authenticateUser(user.getEmail(), user.getPassword());
//        return ResponseEntity.of(Optional.ofNullable(userModel));
//    }

    @GetMapping(value = "/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("hello");
    }


 }
