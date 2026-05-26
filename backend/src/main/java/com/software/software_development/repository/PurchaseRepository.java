package com.software.software_development.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.software.software_development.model.entity.PurchaseEntity;

public interface PurchaseRepository extends CrudRepository<PurchaseEntity, Long>,
        PagingAndSortingRepository<PurchaseEntity, Long> {

    Optional<PurchaseEntity> findByProductIdAndBuyerId(Long productId, Long buyerId);

    boolean existsByProductIdAndBuyerId(Long productId, Long buyerId);

    Page<PurchaseEntity> findByBuyerId(Long buyerId, Pageable pageable);
}
