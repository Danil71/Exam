package com.software.software_development.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.software.software_development.model.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    Optional<UserEntity> findByLoginIgnoreCase(String login);

    @Query("SELECT u FROM UserEntity u WHERE u.employee.id = :employeeId")
    Optional<UserEntity> findByEmployeeId(@Param("employeeId") Long employeeId);
}
