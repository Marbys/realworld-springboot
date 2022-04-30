package io.github.marbys.myrealworldapp.user;

import lombok.NonNull;
import lombok.Value;

import java.util.Optional;

@Value
public class UserPutDTO {

    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;

    public Optional<String> getEmailToUpdate() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getUsernameToUpdate() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getPasswordToUpdate() {
        return Optional.ofNullable(password);
    }

    public Optional<String> getBioToUpdate() {
        return Optional.ofNullable(bio);
    }

    public Optional<String> getImageToUpdate() {
        return Optional.ofNullable(image);
    }
}
