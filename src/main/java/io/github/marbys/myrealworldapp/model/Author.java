package io.github.marbys.myrealworldapp.model;

import lombok.Data;


@Data
public class Author {
    private String username;
    private String bio;
    private String image;
    private boolean following;
}

