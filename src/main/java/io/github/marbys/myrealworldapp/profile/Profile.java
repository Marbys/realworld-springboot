package io.github.marbys.myrealworldapp.profile;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.marbys.myrealworldapp.user.UserEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@JsonTypeName("profile")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
public class Profile {
    @Column(name = "name")
    @NonNull
    private String username;
    private String bio;
    private String image;

    @Transient
    private boolean following;
}

