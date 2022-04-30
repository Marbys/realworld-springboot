package io.github.marbys.myrealworldapp.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class Article {
    private String slug;
    private String title;
    private String description;
    private String body;
    private ArrayList<String> tagList;
    private Date createdAt;
    private Date updatedAt;
    private boolean favorited;
    private int favoritesCount;
    private Author author;
}

