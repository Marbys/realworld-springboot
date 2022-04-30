package io.github.marbys.myrealworldapp.user;

import io.github.marbys.myrealworldapp.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private JwtTokenUtil jwtTokenUtil;
    private AuthenticationManager authenticationManager;
    private UserMapper mapper;
    private UserRepository repository;
    private PasswordEncoder encoder;

    public UserServiceImpl(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, UserRepository repository, UserMapper mapper, PasswordEncoder encoder) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Override
    public UserModel register(UserPostDTO user) {
        if (repository.existsByEmail(user.getEmail()))
            throw new RuntimeException("The email address is taken.");

        UserEntity entity = mapper.userPostDTOToUserEntity(user);
        entity.setPassword(encoder.encode(entity.getPassword()));
        repository.save(entity);
        
        UserModel userModel = mapper.userEntityToUserModel(entity);
        userModel.setToken(jwtTokenUtil.generateToken(userModel.getEmail()));
        return userModel;
    }

    @Override
    public UserModel authenticateUser(String email, String password) {
        UserEntity entity = repository.findByEmail(email)
                .filter(e -> e.matchesPassword(password, encoder))
                .orElseThrow(() -> new BadCredentialsException(""));

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token = jwtTokenUtil.generateToken(email);

        UserModel userModel = mapper.userEntityToUserModel(entity);
        userModel.setToken(token);

        return userModel;
    }

    @Override
    public UserModel updateUser(UserPutDTO userPutDTO, String token) {
        String emailFromToken = jwtTokenUtil.getEmailFromToken(token.substring(7));
        if (!userPutDTO.getEmail().equals(emailFromToken))
            if (repository.existsByEmail(userPutDTO.getEmail())) {
                return null;
            }

        UserEntity userEntity = repository.findByEmail(emailFromToken).get();
        repository.save(updateUser(userPutDTO, userEntity));

        UserModel userModel = mapper.userEntityToUserModel(userEntity);
        String newToken = jwtTokenUtil.generateToken(userEntity.getEmail());
        userModel.setToken(newToken);
        return userModel;
    }


    private UserEntity updateUser(UserPutDTO userPutDTO, UserEntity userEntity) {
        userPutDTO.getEmailToUpdate().ifPresent(userEntity::setEmail);
        userPutDTO.getUsernameToUpdate().ifPresent(userEntity::setUsername);
        userPutDTO.getPasswordToUpdate().ifPresent(s -> userEntity.setPassword(encoder.encode(s)));
        userPutDTO.getBioToUpdate().ifPresent(userEntity::setBio);
        userPutDTO.getImageToUpdate().ifPresent(userEntity::setImage);
        return userEntity;
    }
}
