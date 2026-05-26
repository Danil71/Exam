package com.software.software_development.service.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.CategoryEntity;
import com.software.software_development.model.entity.ProductEntity;
import com.software.software_development.model.entity.RatingEntity;
import com.software.software_development.model.entity.ReviewEntity;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.repository.ProductRepository;
import com.software.software_development.repository.RatingRepository;
import com.software.software_development.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService extends AbstractEntityService<ProductEntity> {

    private final ProductRepository repository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<ProductEntity> getAll(ProductFilterParams params, int page, int size) {
        return repository.findAll(ProductSpecification.withFilters(params), PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public ProductEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProductEntity.class, id));
    }

    @Transactional
    public ProductEntity create(ProductEntity entity, Long ownerId, List<Long> categoryIds) {
        entity.setOwner(resolveOwner(ownerId));
        validate(entity, null);
        syncCategories(entity, categoryIds);
        return repository.save(entity);
    }

    @Transactional
    public ProductEntity update(long id, ProductEntity entity, Long ownerId, List<Long> categoryIds) {
        ProductEntity existing = get(id);
        existing.setName(entity.getName());
        existing.setDescription(entity.getDescription());
        existing.setPrice(entity.getPrice());
        existing.setStock(entity.getStock());
        existing.setOwner(resolveOwner(ownerId));
        validate(existing, id);
        syncCategories(existing, categoryIds);
        return repository.save(existing);
    }

    @Transactional
    public ProductEntity delete(long id) {
        ProductEntity existing = get(id);
        existing.getCategories().forEach(category -> category.getProducts().remove(existing));
        List<ReviewEntity> reviews = reviewRepository.findAllByProductId(id);
        reviews.forEach(reviewRepository::delete);
        List<RatingEntity> ratings = ratingRepository.findAllByProductId(id);
        ratings.forEach(ratingRepository::delete);
        existing.getReviews().clear();
        existing.getRatings().clear();
        repository.delete(existing);
        return existing;
    }

    @Transactional
    public void refreshSearchFields() {
        repository.findAllList().forEach(product -> {
            product.normalizeSearchFields();
            repository.save(product);
        });
    }

    private UserEntity resolveOwner(Long ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Product owner is required");
        }
        return userService.get(ownerId);
    }

    private void syncCategories(ProductEntity product, List<Long> categoryIds) {
        Set<CategoryEntity> newCategories = categoryService.getByIds(categoryIds);
        Set<CategoryEntity> toRemove = new HashSet<>(product.getCategories());
        toRemove.removeAll(newCategories);
        toRemove.forEach(category -> {
            product.getCategories().remove(category);
            category.getProducts().remove(product);
        });
        for (CategoryEntity category : newCategories) {
            product.getCategories().add(category);
            category.getProducts().add(product);
        }
    }

    @Override
    protected void validate(ProductEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Product entity is null");
        }
        validateStringField(entity.getName(), "Product name");
        if (entity.getName().length() > 100) {
            throw new IllegalArgumentException("Product name must be 1-100 characters");
        }
        validateStringField(entity.getDescription(), "Product description");
        if (entity.getDescription().length() > 1000) {
            throw new IllegalArgumentException("Product description must be 1-1000 characters");
        }
        if (entity.getPrice() == null || entity.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        if (entity.getStock() < 0) {
            throw new IllegalArgumentException("Product stock must be 0 or greater");
        }
        if (entity.getOwner() == null) {
            throw new IllegalArgumentException("Product owner is required");
        }
    }
}
