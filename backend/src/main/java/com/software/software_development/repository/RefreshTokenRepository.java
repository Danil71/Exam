package com.software.software_development.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.software.software_development.core.security.enums.ClientType;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.model.token.RefreshTokenEntity;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByUserAndClientType(UserEntity user, ClientType clientType);

    void deleteByToken(String token);

    void deleteByUserAndClientType(UserEntity user, ClientType clientType);
}
