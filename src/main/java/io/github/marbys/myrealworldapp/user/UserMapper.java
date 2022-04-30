package io.github.marbys.myrealworldapp.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.security.auth.callback.PasswordCallback;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    User userModelToUser(UserModel model);

    UserModel userToUserModel(User user);

    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    UserEntity userToUserEntity(User user);

    @Mappings({
           @Mapping(target = "token", ignore = true)
    })
    UserModel userEntityToUserModel(UserEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    UserEntity userPutDTOToUserEntity(UserPutDTO userPutDTO);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "bio", ignore = true),
            @Mapping(target = "image", ignore = true)
    })
    UserEntity userPostDTOToUserEntity(UserPostDTO user);
}
