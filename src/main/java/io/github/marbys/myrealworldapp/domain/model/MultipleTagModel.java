package io.github.marbys.myrealworldapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Data
@AllArgsConstructor
public class MultipleTagModel {
  List<String> tags;

  public static MultipleTagModel fromTagList(List<String> tags) {
    return new MultipleTagModel(tags);
  }
}
