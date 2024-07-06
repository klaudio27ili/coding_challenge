package com.libraryManagement.demo.services.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import jakarta.persistence.criteria.Root;

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
}
