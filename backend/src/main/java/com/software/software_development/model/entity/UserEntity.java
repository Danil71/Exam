package com.software.software_development.model.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.software.software_development.model.enums.UserRole;
import com.software.software_development.model.token.RefreshTokenEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String login;

    @Column(nullable = false, unique = true, columnDefinition = "text")
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "owner")
    @OrderBy("id ASC")
    private Set<ProductEntity> products = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @OrderBy("id ASC")
    private Set<ReviewEntity> reviews = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @OrderBy("id ASC")
    private Set<RatingEntity> ratings = new HashSet<>();

    @OneToMany(mappedBy = "buyer")
    @OrderBy("purchasedAt DESC")
    private Set<PurchaseEntity> purchases = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private Set<RefreshTokenEntity> tokens = new HashSet<>();

    public UserEntity(String login, String email, String password, UserRole role) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, email, password, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserEntity other = (UserEntity) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(login, other.login)
                && Objects.equals(email, other.email)
                && Objects.equals(password, other.password)
                && role == other.role;
    }

    @PrePersist
    private void setUp() {
        this.createdAt = LocalDateTime.now(ZoneId.of("+00:00"));
    }
}
