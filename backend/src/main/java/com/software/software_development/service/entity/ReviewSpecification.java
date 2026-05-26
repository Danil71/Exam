package com.software.software_development.service.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

import com.software.software_development.model.entity.ProductEntity;
import com.software.software_development.model.entity.RatingEntity;
import com.software.software_development.model.entity.ReviewEntity;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public final class ReviewSpecification {

    private ReviewSpecification() {
    }

    public static Specification<ReviewEntity> withFilters(ReviewFilterParams params) {
        return (root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }
            List<Predicate> predicates = new ArrayList<>();

            if (params.getAuthorId() != null) {
                predicates.add(cb.equal(root.get("author").get("id"), params.getAuthorId()));
            }

            if (params.getSearch() != null && !params.getSearch().isBlank()) {
                Join<ReviewEntity, ProductEntity> product = root.join("product", JoinType.LEFT);
                String pattern = "%" + params.getSearch().trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(
                        cb.like(root.get("searchTitle"), pattern),
                        cb.like(root.get("searchText"), pattern),
                        cb.like(product.get("searchName"), pattern)
                ));
            }

            if (params.getRating() != null) {
                Join<ReviewEntity, RatingEntity> rating = root.join("rating", JoinType.INNER);
                predicates.add(cb.equal(rating.get("value"), params.getRating()));
            }

            if (params.getDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), params.getDateFrom().atStartOfDay()));
            }
            if (params.getDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), params.getDateTo().atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
