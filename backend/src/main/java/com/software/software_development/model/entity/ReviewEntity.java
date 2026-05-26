package com.software.software_development.model.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
        name = "review",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_id_product", "fk_id_author"})
)
public class ReviewEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "fk_id_product", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "fk_id_author", nullable = false)
    private UserEntity author;

    @OneToOne
    @JoinColumn(name = "fk_id_rating", nullable = false)
    private RatingEntity rating;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ReviewEntity(ProductEntity product, UserEntity author, String title, String text) {
        this.product = product;
        this.author = author;
        this.title = title;
        this.text = text;
    }

    @PrePersist
    private void setUp() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("+00:00"));
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void touch() {
        this.updatedAt = LocalDateTime.now(ZoneId.of("+00:00"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                Optional.ofNullable(product).map(ProductEntity::getId).orElse(null),
                Optional.ofNullable(author).map(UserEntity::getId).orElse(null),
                title,
                text
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
        ReviewEntity other = (ReviewEntity) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(
                Optional.ofNullable(product).map(ProductEntity::getId).orElse(null),
                Optional.ofNullable(other.product).map(ProductEntity::getId).orElse(null)
        )
                && Objects.equals(
                Optional.ofNullable(author).map(UserEntity::getId).orElse(null),
                Optional.ofNullable(other.author).map(UserEntity::getId).orElse(null)
        )
                && Objects.equals(title, other.title)
                && Objects.equals(text, other.text);
    }
}
