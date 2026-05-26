package com.software.software_development.service.entity;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.core.utility.ValidationUtils;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.model.enums.UserRole;
import com.software.software_development.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService extends AbstractEntityService<UserEntity> {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public UserEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserEntity.class, id));
    }

    @Transactional(readOnly = true)
    public UserEntity getByLogin(String login) {
        return repository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new IllegalArgumentException("User with login " + login + " not found"));
    }

    @Transactional(readOnly = true)
    public UserEntity getByEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));
    }

    @Transactional
    public UserEntity create(UserEntity entity) {
        validate(entity, null);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return repository.save(entity);
    }

    @Transactional
    public UserEntity register(String login, String email, String password) {
        if (repository.findByLoginIgnoreCase(login).isPresent()) {
            throw new IllegalArgumentException("User with login " + login + " already exists");
        }
        if (repository.findByEmailIgnoreCase(email).isPresent()) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        UserEntity user = new UserEntity(login, email, password, UserRole.USER);
        validate(user, null);
        user.setPassword(passwordEncoder.encode(password));
        return repository.save(user);
    }

    @Transactional
    public UserEntity update(long id, UserEntity entity) {
        validate(entity, id);
        UserEntity existing = get(id);
        existing.setLogin(entity.getLogin());
        existing.setEmail(entity.getEmail());
        if (entity.getPassword() != null && !entity.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(entity.getPassword()));
        }
        existing.setRole(entity.getRole());
        return repository.save(existing);
    }

    @Transactional
    public UserEntity delete(long id) {
        UserEntity existing = get(id);
        if (!existing.getProducts().isEmpty() || !existing.getReviews().isEmpty() || !existing.getRatings().isEmpty()) {
            throw new IllegalArgumentException("User has products, reviews or ratings and cannot be deleted");
        }
        repository.delete(existing);
        return existing;
    }

    @Override
    protected void validate(UserEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("User entity is null");
        }
        validateStringField(entity.getLogin(), "User login");
        if (!entity.getLogin().matches(Constants.LOGIN_PATTERN)) {
            throw new IllegalArgumentException("Login has invalid format: " + entity.getLogin());
        }
        validateStringField(entity.getEmail(), "User email");
        ValidationUtils.validateEmailFormat(entity.getEmail());

        boolean isCreate = id == null;
        boolean isPasswordProvided = entity.getPassword() != null && !entity.getPassword().isBlank();
        if (isCreate || isPasswordProvided) {
            validateStringField(entity.getPassword(), "User password");
            if (!entity.getPassword().matches(Constants.PASSWORD_PATTERN)) {
                throw new IllegalArgumentException("Password must be 6-60 characters");
            }
        }
        if (entity.getRole() == null) {
            throw new IllegalArgumentException("User role must not be null");
        }

        repository.findByLoginIgnoreCase(entity.getLogin()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("User with login " + entity.getLogin() + " already exists");
            }
        });
        repository.findByEmailIgnoreCase(entity.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("User with email " + entity.getEmail() + " already exists");
            }
        });
    }
}
