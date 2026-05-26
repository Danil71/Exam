package com.software.software_development.web.dto.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.software.software_development.core.configuration.Constants;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.NAME_PATTERN, message = "Name must be 1-50 characters")
    private String name;

    @NotBlank
    @Pattern(regexp = "^.{1,50}$", message = "Position must be 1-50 characters")
    private String position;

    @Min(value = 1, message = "Department ID must be at least 1")
    private Long departmentId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String departmentName;

    private List<Long> phoneNumberIds = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PhoneNumberDto> phoneNumbers = new ArrayList<>();
}
