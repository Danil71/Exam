package com.software.software_development.model.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.software.software_development.model.enums.UserRole;
import com.software.software_development.model.token.RefreshTokenEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "fk_id_employee")
    private EmployeeEntity employee;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private Set<RefreshTokenEntity> tokens = new HashSet<>();

    public UserEntity(String login, String email, String password, UserRole role, EmployeeEntity employee) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
        this.employee = employee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, login, email, password, role,
                Optional.ofNullable(employee).map(EmployeeEntity::getId).orElse(null)
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
        UserEntity other = (UserEntity) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(login, other.login)
                && Objects.equals(email, other.email)
                && Objects.equals(password, other.password)
                && role == other.role
                && Objects.equals(
                Optional.ofNullable(employee).map(EmployeeEntity::getId).orElse(null),
                Optional.ofNullable(other.employee).map(EmployeeEntity::getId).orElse(null)
        );
    }

    @PrePersist
    private void setUp() {
        this.createdAt = LocalDateTime.now(ZoneId.of("+00:00"));
    }
}
