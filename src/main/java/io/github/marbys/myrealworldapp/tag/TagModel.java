package io.github.marbys.myrealworldapp.tag;

import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class TagModel {
  private String tag;

  public static String fromTag(Tag tag) {
    return tag.getName();
  }

  public static Set<String> fromTagSet(Set<Tag> tags) {
    return tags.stream().map(Tag::getName).collect(Collectors.toSet());
  }
}
