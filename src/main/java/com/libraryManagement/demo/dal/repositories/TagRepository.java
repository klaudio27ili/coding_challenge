package com.libraryManagement.demo.dal.repositories;

import com.libraryManagement.demo.dal.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByNameIgnoreCase(String name);
    @Query("select t from Tag t where t.name IN (:tagNames)")
    List<Tag> findByNames(List<String> tagNames);
}
