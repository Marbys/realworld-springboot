package io.github.marbys.myrealworldapp.tag;

import lombok.Value;

import java.util.List;

@Value
public class TagsModel {
  List<String> tags;

  public static TagsModel fromTagList(List<String> tags) {
    return new TagsModel(tags);
  }
}
