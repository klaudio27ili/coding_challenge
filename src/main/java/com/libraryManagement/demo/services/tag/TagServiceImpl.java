package com.libraryManagement.demo.services.tag;

import com.libraryManagement.demo.dal.entities.Tag;
import com.libraryManagement.demo.dal.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    @Override
    public Set<Tag> findByNameOrSave(List<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        tagNames.forEach(tag -> {
            Optional<Tag> tagOptional = tagRepository.findByName(tag);
            tags.add(tagOptional.orElseGet(() -> tagRepository.save(
                    new Tag(tag)
            )));
        });
        return tags;
    }

    @Override
    public Set<Tag> findByNames(List<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        tagNames.forEach(tag -> {
                    Optional<Tag> tagOptional = tagRepository.findByName(tag);
                    tagOptional.ifPresent(tags::add);
                }
        );
        return tags;
    }
}
