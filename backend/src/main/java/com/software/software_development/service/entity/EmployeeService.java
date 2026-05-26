package com.software.software_development.service.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.model.entity.PhoneNumberEntity;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.model.enums.UserRole;
import com.software.software_development.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService extends AbstractEntityService<EmployeeEntity> {

    private final EmployeeRepository repository;
    private final DepartmentService departmentService;
    private final PhoneNumberService phoneNumberService;

    @Transactional(readOnly = true)
    public Page<EmployeeEntity> getAll(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (name == null || name.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeEntity> getFiltered(EmployeeFilterParams params, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(EmployeeSpecification.withFilters(params), pageable);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeEntity> getMyDepartment(UserEntity user, EmployeeFilterParams params, int page, int size) {
        if (user.getEmployee() == null || user.getEmployee().getDepartment() == null) {
            if (user.getRole() == UserRole.MANAGER) {
                return getPublicDirectory(params, page, size);
            }
            throw new IllegalArgumentException("Текущий пользователь не привязан к сотруднику и отделу");
        }
        Long deptId = user.getEmployee().getDepartment().getId();
        EmployeeFilterParams restricted = EmployeeFilterParams.builder()
                .search(params.getSearch())
                .departmentId(params.getDepartmentId())
                .position(params.getPosition())
                .phone(params.getPhone())
                .extensionMin(params.getExtensionMin())
                .extensionMax(params.getExtensionMax())
                .restrictToDepartmentId(deptId)
                .build();
        return getFiltered(restricted, page, size);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeEntity> getPublicDirectory(EmployeeFilterParams params, int page, int size) {
        return getFiltered(params, page, size);
    }

    @Transactional(readOnly = true)
    public EmployeeEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(EmployeeEntity.class, id));
    }

    @Transactional
    public EmployeeEntity create(EmployeeEntity entity, List<Long> phoneNumberIds) {
        validate(entity, null);
        syncPhones(entity, phoneNumberIds);
        return repository.save(entity);
    }

    @Transactional
    public EmployeeEntity update(long id, EmployeeEntity entity, List<Long> phoneNumberIds) {
        validate(entity, id);
        EmployeeEntity existing = get(id);
        existing.setName(entity.getName());
        existing.setPosition(entity.getPosition());
        existing.setDepartment(entity.getDepartment());
        syncPhones(existing, phoneNumberIds);
        return repository.save(existing);
    }

    @Transactional
    public EmployeeEntity delete(long id) {
        EmployeeEntity existing = get(id);
        new HashSet<>(existing.getPhoneNumbers()).forEach(existing::removePhoneNumber);
        repository.delete(existing);
        return existing;
    }

    private void syncPhones(EmployeeEntity employee, List<Long> phoneNumberIds) {
        Set<PhoneNumberEntity> newPhones = phoneNumberService.getByIds(phoneNumberIds);
        Set<PhoneNumberEntity> toRemove = new HashSet<>(employee.getPhoneNumbers());
        toRemove.removeAll(newPhones);
        toRemove.forEach(employee::removePhoneNumber);
        for (PhoneNumberEntity phone : newPhones) {
            employee.addPhoneNumber(phone);
        }
    }

    public EmployeeEntity bindDepartment(EmployeeEntity entity, Long departmentId) {
        if (departmentId != null) {
            DepartmentEntity department = departmentService.get(departmentId);
            entity.setDepartment(department);
        } else {
            entity.setDepartment(null);
        }
        return entity;
    }

    @Override
    protected void validate(EmployeeEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Employee entity is null");
        }
        validateStringField(entity.getName(), "Employee name");
        if (!entity.getName().matches(Constants.NAME_PATTERN)) {
            throw new IllegalArgumentException("Employee name has invalid format: " + entity.getName());
        }
        validateStringField(entity.getPosition(), "Employee position");
        if (entity.getDepartment() == null) {
            throw new IllegalArgumentException("Employee department must not be null");
        }
    }
}
