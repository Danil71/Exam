package com.software.software_development.service.entity;

import org.springframework.data.jpa.domain.Specification;

import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.model.entity.PhoneNumberEntity;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<EmployeeEntity> withFilters(EmployeeFilterParams params) {
        return (root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }
            List<Predicate> predicates = new ArrayList<>();

            if (params.getRestrictToDepartmentId() != null) {
                Join<EmployeeEntity, DepartmentEntity> dept = root.join("department", JoinType.INNER);
                predicates.add(cb.equal(dept.get("id"), params.getRestrictToDepartmentId()));
            } else if (params.getDepartmentId() != null) {
                Join<EmployeeEntity, DepartmentEntity> dept = root.join("department", JoinType.LEFT);
                predicates.add(cb.equal(dept.get("id"), params.getDepartmentId()));
            }

            if (params.getSearch() != null && !params.getSearch().isBlank()) {
                String pattern = "%" + params.getSearch().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), pattern));
            }

            if (params.getOwner() != null && !params.getOwner().isBlank()) {
                String pattern = "%" + params.getOwner().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), pattern));
            }

            if (params.getPosition() != null && !params.getPosition().isBlank()) {
                String pattern = "%" + params.getPosition().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("position")), pattern));
            }

            if (params.getPhone() != null && !params.getPhone().isBlank()) {
                Join<EmployeeEntity, PhoneNumberEntity> phones = root.join("phoneNumbers", JoinType.LEFT);
                String pattern = "%" + params.getPhone().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(phones.get("number")), pattern));
            }

            if (params.getExtensionMin() != null || params.getExtensionMax() != null) {
                Join<EmployeeEntity, PhoneNumberEntity> phones = root.join("phoneNumbers", JoinType.LEFT);
                if (params.getExtensionMin() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(phones.get("extension"), params.getExtensionMin()));
                }
                if (params.getExtensionMax() != null) {
                    predicates.add(cb.lessThanOrEqualTo(phones.get("extension"), params.getExtensionMax()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
