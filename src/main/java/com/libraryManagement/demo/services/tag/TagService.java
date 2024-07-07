package com.libraryManagement.demo.services.tag;

import com.libraryManagement.demo.dal.entities.Tag;
import java.util.List;
import java.util.Set;

public interface TagService {
    Set<Tag> findByNameOrSave(List<String> tagName);
    Set<Tag> findByNames(List<String> tagNames);
}
