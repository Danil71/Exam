package com.software.software_development.web.dto.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^.{1,100}$", message = "Product name must be 1-100 characters")
    private String name;

    @NotBlank
    @Pattern(regexp = "^.{1,1000}$", message = "Product description must be 1-1000 characters")
    private String description;

    @NotNull
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Product stock must be 0 or greater")
    private int stock;

    @NotNull
    @Min(value = 1, message = "Owner ID must be at least 1")
    private Long ownerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String ownerLogin;

    private List<Long> categoryIds = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CategoryDto> categories = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double averageRating;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int reviewCount;
}
