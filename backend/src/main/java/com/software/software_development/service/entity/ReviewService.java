package com.software.software_development.service.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.ProductEntity;
import com.software.software_development.model.entity.RatingEntity;
import com.software.software_development.model.entity.ReviewEntity;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService extends AbstractEntityService<ReviewEntity> {

    private final ReviewRepository repository;
    private final ProductService productService;
    private final UserService userService;
    private final RatingService ratingService;
    private final PurchaseService purchaseService;

    @Transactional(readOnly = true)
    public Page<ReviewEntity> getMy(ReviewFilterParams params, int page, int size) {
        return repository.findAll(ReviewSpecification.withFilters(params), PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<ReviewEntity> getByProduct(long productId, int page, int size) {
        productService.get(productId);
        return repository.findByProductId(productId, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public ReviewEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ReviewEntity.class, id));
    }

    @Transactional
    public ReviewEntity create(ReviewEntity entity, Long productId, Long authorId, int ratingValue) {
        ProductEntity product = productService.get(productId);
        UserEntity author = userService.get(authorId);
        validatePurchased(productId, authorId);
        repository.findByProductIdAndAuthorId(productId, authorId).ifPresent(existing -> {
            throw new IllegalArgumentException("You have already reviewed this product");
        });
        RatingEntity rating = ratingService.upsert(product, author, ratingValue);
        entity.setProduct(product);
        entity.setAuthor(author);
        entity.setRating(rating);
        validate(entity, null);
        return repository.save(entity);
    }

    @Transactional
    public ReviewEntity update(long id, ReviewEntity entity, Long authorId, int ratingValue) {
        ReviewEntity existing = getOwned(id, authorId);
        validatePurchased(existing.getProduct().getId(), authorId);
        existing.setTitle(entity.getTitle());
        existing.setText(entity.getText());
        existing.setRating(ratingService.upsert(existing.getProduct(), existing.getAuthor(), ratingValue));
        validate(existing, id);
        return repository.save(existing);
    }

    @Transactional
    public ReviewEntity delete(long id, Long authorId) {
        ReviewEntity existing = getOwned(id, authorId);
        RatingEntity rating = existing.getRating();
        repository.delete(existing);
        if (rating != null) {
            ratingService.delete(rating.getId());
        }
        return existing;
    }

    @Transactional
    public void refreshSearchFields() {
        repository.findAllList().forEach(review -> {
            review.normalizeSearchFields();
            repository.save(review);
        });
    }

    @Transactional
    public void ensurePurchasesForExistingReviews() {
        repository.findAllList().forEach(review ->
                purchaseService.ensureExists(review.getProduct(), review.getAuthor())
        );
    }

    private ReviewEntity getOwned(long id, Long authorId) {
        ReviewEntity review = get(id);
        if (!review.getAuthor().getId().equals(authorId)) {
            throw new IllegalArgumentException("You can edit only your own reviews");
        }
        return review;
    }

    private void validatePurchased(Long productId, Long authorId) {
        if (!purchaseService.hasPurchased(productId, authorId)) {
            throw new IllegalArgumentException("You can review only purchased products");
        }
    }

    @Override
    protected void validate(ReviewEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Review entity is null");
        }
        if (entity.getProduct() == null) {
            throw new IllegalArgumentException("Review product is required");
        }
        if (entity.getAuthor() == null) {
            throw new IllegalArgumentException("Review author is required");
        }
        if (entity.getRating() == null) {
            throw new IllegalArgumentException("Review rating is required");
        }
        validateStringField(entity.getTitle(), "Review title");
        if (entity.getTitle().length() > 100) {
            throw new IllegalArgumentException("Review title must be 1-100 characters");
        }
        validateStringField(entity.getText(), "Review text");
        if (entity.getText().length() > 1000) {
            throw new IllegalArgumentException("Review text must be 1-1000 characters");
        }
    }
}
