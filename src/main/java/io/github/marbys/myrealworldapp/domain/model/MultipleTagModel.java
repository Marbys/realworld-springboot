package io.github.marbys.myrealworldapp.domain.model;

import lombok.*;

import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MultipleTagModel {
  List<String> tags;

  public static MultipleTagModel fromTagList(List<String> tags) {
    return new MultipleTagModel(tags);
  }
}
