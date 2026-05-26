package com.software.software_development.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.software.software_development.model.entity.ReviewEntity;

public interface ReviewRepository extends CrudRepository<ReviewEntity, Long>,
        PagingAndSortingRepository<ReviewEntity, Long>, JpaSpecificationExecutor<ReviewEntity> {

    Optional<ReviewEntity> findByProductIdAndAuthorId(Long productId, Long authorId);

    @Query("SELECT r FROM ReviewEntity r")
    List<ReviewEntity> findAllList();

    List<ReviewEntity> findAllByProductId(Long productId);

    Page<ReviewEntity> findByProductId(Long productId, Pageable pageable);
}
