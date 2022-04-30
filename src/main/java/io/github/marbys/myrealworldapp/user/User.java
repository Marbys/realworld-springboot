package io.github.marbys.myrealworldapp.user;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    @NonNull private String email;
    @NonNull private String username;
    @NonNull private String password;
    private String token;
    private String bio;
    private String image;

}
