package io.github.marbys.myrealworldapp.domain.model;

import io.github.marbys.myrealworldapp.domain.Tag;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TagModel {
  String tag;

  public static String fromTag(Tag tag) {
    return tag.getName();
  }

  public static Set<String> fromTagSet(Set<Tag> tags) {
    return tags.stream().map(Tag::getName).collect(Collectors.toSet());
  }
}
