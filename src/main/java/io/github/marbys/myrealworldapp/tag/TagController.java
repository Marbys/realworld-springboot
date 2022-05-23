package io.github.marbys.myrealworldapp.tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class TagController {

  private final TagService service;

  public TagController(TagService service) {
    this.service = service;
  }

  @GetMapping("/api/tags")
  public ResponseEntity<TagsModel> tags() {
    return ResponseEntity.ok(
        TagsModel.fromTagList(
            service.getAllTags().stream().map(TagModel::fromTag).collect(Collectors.toList())));
  }
}
