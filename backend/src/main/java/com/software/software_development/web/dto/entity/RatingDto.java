package com.software.software_development.web.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Min(1)
    private Long productId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long authorId;

    @Min(1)
    @Max(5)
    private int value;
}
