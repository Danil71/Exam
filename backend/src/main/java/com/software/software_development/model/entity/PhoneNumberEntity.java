package com.software.software_development.model.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.software.software_development.model.enums.PhoneType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phone_number")
public class PhoneNumberEntity extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhoneType type = PhoneType.WORK;

    @Column(nullable = false)
    private int extension;

    @ManyToMany(mappedBy = "phoneNumbers")
    private Set<EmployeeEntity> employees = new HashSet<>();

    public PhoneNumberEntity(String number, PhoneType type, int extension) {
        this.number = number;
        this.type = type;
        this.extension = extension;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, type, extension);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PhoneNumberEntity other = (PhoneNumberEntity) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(number, other.number)
                && type == other.type
                && extension == other.extension;
    }
}
