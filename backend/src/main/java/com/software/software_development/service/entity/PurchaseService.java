package com.software.software_development.service.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.ProductEntity;
import com.software.software_development.model.entity.PurchaseEntity;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.repository.PurchaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService extends AbstractEntityService<PurchaseEntity> {

    private final PurchaseRepository repository;
    private final ProductService productService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<PurchaseEntity> getMy(Long buyerId, int page, int size) {
        return repository.findByBuyerId(
                buyerId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "purchasedAt"))
        );
    }

    @Transactional(readOnly = true)
    public PurchaseEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(PurchaseEntity.class, id));
    }

    @Transactional(readOnly = true)
    public boolean hasPurchased(Long productId, Long buyerId) {
        return repository.existsByProductIdAndBuyerId(productId, buyerId);
    }

    @Transactional
    public PurchaseEntity buy(Long productId, Long buyerId) {
        repository.findByProductIdAndBuyerId(productId, buyerId).ifPresent(existing -> {
            throw new IllegalArgumentException("Product is already in your purchases");
        });
        ProductEntity product = productService.get(productId);
        if (product.getStock() <= 0) {
            throw new IllegalArgumentException("Product is out of stock");
        }
        UserEntity buyer = userService.get(buyerId);
        product.setStock(product.getStock() - 1);
        PurchaseEntity purchase = new PurchaseEntity(product, buyer);
        validate(purchase, null);
        return repository.save(purchase);
    }

    @Transactional
    public PurchaseEntity ensureExists(ProductEntity product, UserEntity buyer) {
        return repository.findByProductIdAndBuyerId(product.getId(), buyer.getId())
                .orElseGet(() -> {
                    PurchaseEntity purchase = new PurchaseEntity(product, buyer);
                    validate(purchase, null);
                    return repository.save(purchase);
                });
    }

    @Override
    protected void validate(PurchaseEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Purchase entity is null");
        }
        if (entity.getProduct() == null) {
            throw new IllegalArgumentException("Purchase product is required");
        }
        if (entity.getBuyer() == null) {
            throw new IllegalArgumentException("Purchase buyer is required");
        }
    }
}
