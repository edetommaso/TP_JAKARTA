package com.example.tpjakarta.repositories;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.utils.AnnonceStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AnnonceSpecifications {

    public static Specification<Annonce> search(String q, AnnonceStatus status, Long categoryId, Long authorId, Timestamp fromDate, Timestamp toDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Free text search on title or description
            if (q != null && !q.trim().isEmpty()) {
                String likePattern = "%" + q.trim().toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern);
                predicates.add(criteriaBuilder.or(titlePredicate, descPredicate));
            }

            // Exact match on status
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // Exact match on categoryId
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.join("category").get("id"), categoryId));
            }

            // Exact match on authorId
            if (authorId != null) {
                predicates.add(criteriaBuilder.equal(root.join("author").get("id"), authorId));
            }

            // Date range
            if (fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), fromDate));
            }
            if (toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), toDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
