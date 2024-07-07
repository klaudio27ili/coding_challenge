package com.libraryManagement.demo.services.utils;

import com.libraryManagement.demo.dal.entities.Book;
import com.libraryManagement.demo.dal.entities.Tag;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SpecificationService<ENTITY> {
    protected Specification<ENTITY> jsonArrayContains(List<String> tags) {
        return (Root<ENTITY> root,CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate[] predicates = new Predicate[tags.size()];
            for(int i = 0; i < tags.size(); i++) {
                predicates[i] = criteriaBuilder.isTrue(
                        criteriaBuilder.function(
                                "jsonb_exists",
                                Boolean.class,
                                root.get("tags"),
                                criteriaBuilder.literal(tags.get(i))
                        )
                );
            }
            return criteriaBuilder.and(predicates);
        };
    }

    protected Specification<ENTITY> hasTagsSpecification(Set<Tag> tags) {
        return (Root<ENTITY> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Join<Book, Tag> tagJoin = root.join("tags");

            Set<UUID> tagIds = new HashSet<>();
            for(Tag tag : tags) {
                tagIds.add(tag.getId());
            }

            query.groupBy(root.get("id"));
            query.having(cb.equal(cb.count(tagJoin), tags.size()));

            Predicate[] predicates = new Predicate[tagIds.size()];
            int i = 0;
            for(UUID tagId : tagIds) {
                predicates[i++] = cb.equal(tagJoin.get("id"), tagId);
            }

            return cb.and(predicates);
        };
    }
}
