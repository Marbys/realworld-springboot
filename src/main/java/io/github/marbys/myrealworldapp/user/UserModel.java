package io.github.marbys.myrealworldapp.user;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private String email;
    private String username;
    private String token;
    private String bio;
    private String image;



}
