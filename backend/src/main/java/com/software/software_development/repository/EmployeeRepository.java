package com.software.software_development.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.software.software_development.model.entity.EmployeeEntity;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long>,
        PagingAndSortingRepository<EmployeeEntity, Long>, JpaSpecificationExecutor<EmployeeEntity> {

    Optional<EmployeeEntity> findByNameIgnoreCase(String name);

    @Query("""
            SELECT e FROM EmployeeEntity e
            WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    List<EmployeeEntity> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("""
            SELECT e FROM EmployeeEntity e
            WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<EmployeeEntity> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
}
