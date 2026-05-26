package com.software.software_development.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.software.software_development.model.entity.RatingEntity;

public interface RatingRepository extends CrudRepository<RatingEntity, Long>,
        PagingAndSortingRepository<RatingEntity, Long> {

    Optional<RatingEntity> findByProductIdAndAuthorId(Long productId, Long authorId);

    List<RatingEntity> findAllByProductId(Long productId);
}
