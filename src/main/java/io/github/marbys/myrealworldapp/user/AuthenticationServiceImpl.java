package io.github.marbys.myrealworldapp.user;

import io.github.marbys.myrealworldapp.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private JwtTokenUtil jwtTokenUtil;
    private AuthenticationManager authenticationManager;
    private UserMapper mapper;
    private UserRepository repository;
    private PasswordEncoder encoder;

    public AuthenticationServiceImpl(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, UserRepository repository, UserMapper mapper, PasswordEncoder encoder) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Override
    public UserModel authenticateUser(String email, String password) {
        UserEntity entity = repository.findByEmail(email)
                .filter(e -> e.matchesPassword(password, encoder))
                .orElseThrow(() -> new BadCredentialsException(""));


        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, null));
        UserModel userModel = mapper.userEntityToUserModel(entity);
        userModel.setToken(jwtTokenUtil.generateToken(email));

        return userModel;
    }



}
