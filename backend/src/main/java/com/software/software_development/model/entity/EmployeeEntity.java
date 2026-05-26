package com.software.software_development.model.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class EmployeeEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String position;

    @ManyToOne
    @JoinColumn(name = "fk_id_department")
    private DepartmentEntity department;

    @ManyToMany
    @JoinTable(
            name = "employee_phone",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "phone_number_id")
    )
    private Set<PhoneNumberEntity> phoneNumbers = new HashSet<>();

    public EmployeeEntity(String name, String position, DepartmentEntity department) {
        this.name = name;
        this.position = position;
        this.department = department;
    }

    public void addPhoneNumber(PhoneNumberEntity phone) {
        phoneNumbers.add(phone);
        phone.getEmployees().add(this);
    }

    public void removePhoneNumber(PhoneNumberEntity phone) {
        phoneNumbers.remove(phone);
        phone.getEmployees().remove(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, name, position,
                Optional.ofNullable(department).map(DepartmentEntity::getId).orElse(null)
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
        EmployeeEntity other = (EmployeeEntity) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(position, other.position)
                && Objects.equals(
                Optional.ofNullable(department).map(DepartmentEntity::getId).orElse(null),
                Optional.ofNullable(other.department).map(DepartmentEntity::getId).orElse(null)
        );
    }
}
