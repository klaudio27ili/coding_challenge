package com.libraryManagement.demo.services.tag;

import com.libraryManagement.demo.BaseLibraryManagementTest;
import com.libraryManagement.demo.dal.entities.Tag;
import com.libraryManagement.demo.dal.repositories.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Set;

public class TagServiceTests extends BaseLibraryManagementTest {
    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void testSavedMissingTag() {
        List<String> tagNames = List.of("missing-tag1");
        Set<Tag> tags = tagService.findByNameOrSave(tagNames);
        Assertions.assertEquals(tags.size(), 1);
        Assertions.assertEquals(tags.stream().toList().get(0).getName(), "missing-tag1");
    }

    @Test
    public void saveExistingTag_thenReturnCurrent() {
        Tag newTag = tagRepository.save(new Tag("New Tag"));
        Set<Tag> tags = tagService.findByNameOrSave(List.of(newTag.getName()));
        Assertions.assertEquals(tags.size(), 1);
        Tag returnedTag = tags.stream().toList().get(0);
        Assertions.assertEquals(returnedTag.getName(), "New Tag");
        Assertions.assertEquals(returnedTag.getId(), newTag.getId());
    }

    @Test
    public void whenSearchingNonExistingTag_thenReturnNothing() {
        Set<Tag> tags = tagService.findByNames(List.of("non existing tag"));
        Assertions.assertEquals(tags.size(), 0);
    }

    @Test
    public void whenSearchingExistingTag_thenReturnTag() {
        Tag existingTag = tagRepository.save(new Tag("existing tag"));
        Set<Tag> tags = tagService.findByNames(List.of("existing tag"));
        Tag returnedTag = tags.stream().toList().get(0);
        Assertions.assertEquals(tags.size(), 1);
        Assertions.assertEquals(returnedTag.getName(), "existing tag");
        Assertions.assertEquals(existingTag.getId(), returnedTag.getId());
    }
}
