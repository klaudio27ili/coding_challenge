package com.libraryManagement.demo.services.tag;

import com.libraryManagement.demo.dal.entities.Tag;
import com.libraryManagement.demo.dal.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    @Override
    public Set<Tag> findByNameOrSave(List<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        tagNames.forEach(tag -> {Optional<Tag> tagOptional = tagRepository.findByNameIgnoreCase(tag);
            tags.add(tagOptional.orElseGet(() -> tagRepository.save(
                    new Tag(tag)
            )));
        });
        return tags;
    }

    @Override
    public Set<Tag> findByNames(List<String> tagNames) {
        return new HashSet<>(tagRepository.findByNames(tagNames));
    }

}
