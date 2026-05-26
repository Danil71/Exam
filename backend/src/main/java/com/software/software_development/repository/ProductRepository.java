package com.software.software_development.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.software.software_development.model.entity.ProductEntity;

public interface ProductRepository extends CrudRepository<ProductEntity, Long>,
        PagingAndSortingRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    @Query("SELECT p FROM ProductEntity p")
    List<ProductEntity> findAllList();
}
