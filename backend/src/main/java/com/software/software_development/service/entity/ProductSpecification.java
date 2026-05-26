package com.software.software_development.service.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.software.software_development.model.entity.CategoryEntity;
import com.software.software_development.model.entity.ProductEntity;
import com.software.software_development.model.entity.RatingEntity;
import com.software.software_development.model.entity.UserEntity;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<ProductEntity> withFilters(ProductFilterParams params) {
        return (root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }
            List<Predicate> predicates = new ArrayList<>();

            if (params.getSearch() != null && !params.getSearch().isBlank()) {
                String pattern = "%" + params.getSearch().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), pattern));
            }

            if (params.getOwner() != null && !params.getOwner().isBlank()) {
                Join<ProductEntity, UserEntity> owner = root.join("owner", JoinType.LEFT);
                String pattern = "%" + params.getOwner().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(owner.get("login")), pattern));
            }

            if (params.getCategoryId() != null) {
                Join<ProductEntity, CategoryEntity> category = root.join("categories", JoinType.INNER);
                predicates.add(cb.equal(category.get("id"), params.getCategoryId()));
            }

            if (params.getMinRating() != null) {
                Subquery<Double> avgQuery = query.subquery(Double.class);
                Root<RatingEntity> rating = avgQuery.from(RatingEntity.class);
                avgQuery.select(cb.avg(rating.get("value")));
                avgQuery.where(cb.equal(rating.get("product"), root));
                predicates.add(cb.greaterThanOrEqualTo(avgQuery, params.getMinRating().doubleValue()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
