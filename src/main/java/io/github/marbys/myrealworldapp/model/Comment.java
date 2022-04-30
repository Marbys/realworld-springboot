package io.github.marbys.myrealworldapp.model;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private int id;
    private Date createdAt;
    private Date updatedAt;
    private String body;
    private Author author;
}
