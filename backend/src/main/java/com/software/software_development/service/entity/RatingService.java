package com.software.software_development.service.entity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.ProductEntity;
import com.software.software_development.model.entity.RatingEntity;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.repository.RatingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService extends AbstractEntityService<RatingEntity> {

    private final RatingRepository repository;

    @Transactional(readOnly = true)
    public RatingEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(RatingEntity.class, id));
    }

    @Transactional
    public RatingEntity upsert(ProductEntity product, UserEntity author, int value) {
        RatingEntity rating = repository.findByProductIdAndAuthorId(product.getId(), author.getId())
                .orElseGet(() -> new RatingEntity(product, author, value));
        rating.setProduct(product);
        rating.setAuthor(author);
        rating.setValue(value);
        validate(rating, rating.getId());
        return repository.save(rating);
    }

    @Transactional
    public RatingEntity delete(long id) {
        RatingEntity existing = get(id);
        repository.delete(existing);
        return existing;
    }

    @Override
    protected void validate(RatingEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Rating entity is null");
        }
        if (entity.getProduct() == null) {
            throw new IllegalArgumentException("Rating product is required");
        }
        if (entity.getAuthor() == null) {
            throw new IllegalArgumentException("Rating author is required");
        }
        if (entity.getValue() < 1 || entity.getValue() > 5) {
            throw new IllegalArgumentException("Rating value must be from 1 to 5");
        }
    }
}
