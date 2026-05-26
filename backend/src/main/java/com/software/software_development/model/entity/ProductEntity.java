package com.software.software_development.model.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "search_name", length = 100)
    private String searchName;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name = "fk_id_owner", nullable = false)
    private UserEntity owner;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private Set<ReviewEntity> reviews = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private Set<RatingEntity> ratings = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("purchasedAt DESC")
    private Set<PurchaseEntity> purchases = new HashSet<>();

    public ProductEntity(String name, String description, BigDecimal price, int stock, UserEntity owner) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.owner = owner;
    }

    public double getAverageRating() {
        return ratings.stream().mapToInt(RatingEntity::getValue).average().orElse(0.0);
    }

    public int getReviewCount() {
        return reviews.size();
    }

    @PrePersist
    @PreUpdate
    public void normalizeSearchFields() {
        this.searchName = normalize(name);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, stock,
                Optional.ofNullable(owner).map(UserEntity::getId).orElse(null));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ProductEntity other = (ProductEntity) obj;
        return stock == other.stock
                && Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description)
                && Objects.equals(price, other.price)
                && Objects.equals(
                Optional.ofNullable(owner).map(UserEntity::getId).orElse(null),
                Optional.ofNullable(other.owner).map(UserEntity::getId).orElse(null)
        );
    }
}
