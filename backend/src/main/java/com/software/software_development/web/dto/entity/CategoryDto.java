package com.software.software_development.web.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^.{1,60}$", message = "Category name must be 1-60 characters")
    private String name;

    @NotBlank
    @Pattern(regexp = "^.{1,300}$", message = "Category description must be 1-300 characters")
    private String description;
}
