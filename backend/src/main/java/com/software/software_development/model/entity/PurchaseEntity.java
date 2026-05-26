package com.software.software_development.model.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "purchase",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_id_product", "fk_id_buyer"})
)
public class PurchaseEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "fk_id_product", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "fk_id_buyer", nullable = false)
    private UserEntity buyer;

    @Column(name = "purchased_at", nullable = false)
    private LocalDateTime purchasedAt;

    public PurchaseEntity(ProductEntity product, UserEntity buyer) {
        this.product = product;
        this.buyer = buyer;
    }

    @PrePersist
    private void setUp() {
        this.purchasedAt = LocalDateTime.now(ZoneId.of("+00:00"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                Optional.ofNullable(product).map(ProductEntity::getId).orElse(null),
                Optional.ofNullable(buyer).map(UserEntity::getId).orElse(null)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PurchaseEntity other = (PurchaseEntity) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(
                Optional.ofNullable(product).map(ProductEntity::getId).orElse(null),
                Optional.ofNullable(other.product).map(ProductEntity::getId).orElse(null)
        )
                && Objects.equals(
                Optional.ofNullable(buyer).map(UserEntity::getId).orElse(null),
                Optional.ofNullable(other.buyer).map(UserEntity::getId).orElse(null)
        );
    }
}
