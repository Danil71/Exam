package com.software.software_development.service.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.PhoneNumberEntity;
import com.software.software_development.repository.PhoneNumberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhoneNumberService extends AbstractEntityService<PhoneNumberEntity> {

    private final PhoneNumberRepository repository;

    @Transactional(readOnly = true)
    public List<PhoneNumberEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public PhoneNumberEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(PhoneNumberEntity.class, id));
    }

    @Transactional(readOnly = true)
    public Set<PhoneNumberEntity> getByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(StreamSupport.stream(repository.findAllById(ids).spliterator(), false).toList());
    }

    @Transactional
    public PhoneNumberEntity create(PhoneNumberEntity entity) {
        validate(entity, null);
        return repository.save(entity);
    }

    @Transactional
    public PhoneNumberEntity update(long id, PhoneNumberEntity entity) {
        validate(entity, id);
        PhoneNumberEntity existing = get(id);
        existing.setNumber(normalizePhone(entity.getNumber()));
        existing.setType(entity.getType());
        existing.setExtension(entity.getExtension());
        return repository.save(existing);
    }

    @Transactional
    public PhoneNumberEntity delete(long id) {
        PhoneNumberEntity entity = get(id);
        entity.getEmployees().forEach(emp -> emp.removePhoneNumber(entity));
        repository.delete(entity);
        return entity;
    }

    @Override
    protected void validate(PhoneNumberEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Phone number entity is null");
        }
        validateStringField(entity.getNumber(), "Phone number");
        entity.setNumber(normalizePhone(entity.getNumber()));
        if (entity.getType() == null) {
            throw new IllegalArgumentException("Phone type must not be null");
        }
        if (entity.getExtension() < 0) {
            throw new IllegalArgumentException("Extension must be non-negative");
        }
    }

    private String normalizePhone(String phoneNumber) {
        if (!phoneNumber.matches(Constants.PHONE_PATTERN)) {
            throw new IllegalArgumentException("Phone number has invalid format: " + phoneNumber);
        }
        String cleaned = phoneNumber.replaceAll("[\\s\\-()]", "");
        if (cleaned.startsWith("8")) {
            cleaned = "+7" + cleaned.substring(1);
        }
        return cleaned;
    }
}
