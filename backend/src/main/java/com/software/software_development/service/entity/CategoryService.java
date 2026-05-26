package com.software.software_development.service.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.CategoryEntity;
import com.software.software_development.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService extends AbstractEntityService<CategoryEntity> {

    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Page<CategoryEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public CategoryEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(CategoryEntity.class, id));
    }

    @Transactional(readOnly = true)
    public Set<CategoryEntity> getByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        Set<CategoryEntity> result = new HashSet<>();
        for (Long id : ids) {
            result.add(get(id));
        }
        return result;
    }

    @Transactional
    public CategoryEntity create(CategoryEntity entity) {
        validate(entity, null);
        return repository.save(entity);
    }

    @Transactional
    public CategoryEntity update(long id, CategoryEntity entity) {
        validate(entity, id);
        CategoryEntity existing = get(id);
        existing.setName(entity.getName());
        existing.setDescription(entity.getDescription());
        return repository.save(existing);
    }

    @Transactional
    public CategoryEntity delete(long id) {
        CategoryEntity existing = get(id);
        existing.getProducts().forEach(product -> product.getCategories().remove(existing));
        repository.delete(existing);
        return existing;
    }

    @Override
    protected void validate(CategoryEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Category entity is null");
        }
        validateStringField(entity.getName(), "Category name");
        if (entity.getName().length() > 60) {
            throw new IllegalArgumentException("Category name must be 1-60 characters");
        }
        validateStringField(entity.getDescription(), "Category description");
        if (entity.getDescription().length() > 300) {
            throw new IllegalArgumentException("Category description must be 1-300 characters");
        }
        repository.findByNameIgnoreCase(entity.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Category with name " + entity.getName() + " already exists");
            }
        });
    }
}
