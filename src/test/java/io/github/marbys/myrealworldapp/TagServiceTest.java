package io.github.marbys.myrealworldapp;

import io.github.marbys.myrealworldapp.domain.Tag;
import io.github.marbys.myrealworldapp.repository.TagRepository;
import io.github.marbys.myrealworldapp.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

  @Mock private TagRepository tagRepository;

  private TagService tagService;

  @BeforeEach
  public void setUp() {
    tagService = new TagService(tagRepository);
    //        ArrayList<Tag> tags = new ArrayList<>();
    //        tags.add(new Tag("angularJS"));
    //        tags.add(new Tag("mockito"));
    //        tags.add(new Tag("dragon"));
    //        when(tagRepository.findAll()).thenReturn(tags);
    //        tagService = new TagService(tagRepository);
  }

  @Test
  public void save_tag() {
    // given
    Tag tag = new Tag("angularJS");

    // when
    Tag save = tagRepository.save(tag);

    // then
    verify(tagRepository, times(1)).save(tag);
  }

  @Test
  public void get_all_tags() {
    // given
    List<Tag> givenTags =
        Arrays.asList(new Tag("angularJS"), new Tag("mockito"), new Tag("dragon"));

    // when
    when(tagRepository.findAll()).thenReturn(givenTags);
    // then

    Set<Tag> tags = tagService.getAllTags();
    assertEquals(3, tags.size());
  }
}
