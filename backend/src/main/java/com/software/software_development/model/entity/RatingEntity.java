package com.software.software_development.model.entity;

import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
        name = "rating",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_id_product", "fk_id_author"})
)
public class RatingEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "fk_id_product", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "fk_id_author", nullable = false)
    private UserEntity author;

    @Column(nullable = false)
    private int value;

    public RatingEntity(ProductEntity product, UserEntity author, int value) {
        this.product = product;
        this.author = author;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                Optional.ofNullable(product).map(ProductEntity::getId).orElse(null),
                Optional.ofNullable(author).map(UserEntity::getId).orElse(null),
                value
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
        RatingEntity other = (RatingEntity) obj;
        return value == other.value
                && Objects.equals(id, other.id)
                && Objects.equals(
                Optional.ofNullable(product).map(ProductEntity::getId).orElse(null),
                Optional.ofNullable(other.product).map(ProductEntity::getId).orElse(null)
        )
                && Objects.equals(
                Optional.ofNullable(author).map(UserEntity::getId).orElse(null),
                Optional.ofNullable(other.author).map(UserEntity::getId).orElse(null)
        );
    }
}
