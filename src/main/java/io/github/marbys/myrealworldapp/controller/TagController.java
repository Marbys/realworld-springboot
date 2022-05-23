package io.github.marbys.myrealworldapp.controller;

import io.github.marbys.myrealworldapp.domain.model.MultipleTagModel;
import io.github.marbys.myrealworldapp.domain.model.TagModel;
import io.github.marbys.myrealworldapp.service.TagService;
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
  public ResponseEntity<MultipleTagModel> tags() {
    return ResponseEntity.ok(
        MultipleTagModel.fromTagList(
            service.getAllTags().stream().map(TagModel::fromTag).collect(Collectors.toList())));
  }
}
