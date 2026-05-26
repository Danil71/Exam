package com.software.software_development.service.authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.security.enums.ClientType;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.model.token.RefreshTokenEntity;
import com.software.software_development.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void replaceToken(UserEntity user, String newToken, ClientType clientType) {
        refreshTokenRepository.findByUserAndClientType(user, clientType)
                .ifPresentOrElse(
                        existingToken -> {
                            existingToken.setToken(newToken);
                            refreshTokenRepository.save(existingToken);
                        },
                        () -> {
                            RefreshTokenEntity newEntity = new RefreshTokenEntity();
                            newEntity.setUser(user);
                            newEntity.setToken(newToken);
                            newEntity.setClientType(clientType);
                            refreshTokenRepository.save(newEntity);
                        }
                );
    }

    @Transactional
    public void deleteToken(UserEntity user, ClientType clientType) {
        refreshTokenRepository.deleteByUserAndClientType(user, clientType);
    }

    @Transactional(readOnly = true)
    public RefreshTokenEntity validateAndGetToken(String token, ClientType clientType) {
        if (token == null)
            throw new CredentialsExpiredException("Missing token");

        RefreshTokenEntity entity = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token (not found in DB)"));

        if (entity.getClientType() != clientType) {
            throw new BadCredentialsException("Token used for wrong client type");
        }

        return entity;
    }
}
