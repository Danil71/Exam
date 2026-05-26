package com.software.software_development.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.software.software_development.model.entity.CategoryEntity;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long>,
        PagingAndSortingRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByNameIgnoreCase(String name);
}
