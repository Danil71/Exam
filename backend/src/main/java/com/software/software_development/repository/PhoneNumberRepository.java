package com.software.software_development.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.software.software_development.model.entity.PhoneNumberEntity;

public interface PhoneNumberRepository extends CrudRepository<PhoneNumberEntity, Long>,
        PagingAndSortingRepository<PhoneNumberEntity, Long> {

    Optional<PhoneNumberEntity> findByNumber(String number);
}
