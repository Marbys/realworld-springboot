package io.github.marbys.myrealworldapp.application.controller;

import io.github.marbys.myrealworldapp.domain.model.MultipleTagModel;
import io.github.marbys.myrealworldapp.domain.model.TagModel;
import io.github.marbys.myrealworldapp.domain.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {

  private final TagService service;

  @GetMapping("/tags")
  public ResponseEntity<MultipleTagModel> tags() {
    return ResponseEntity.ok(
        MultipleTagModel.fromTagList(
            service.getAllTags().stream().map(TagModel::fromTag).collect(Collectors.toList())));
  }
}
