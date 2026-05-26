package com.software.software_development.service.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductFilterParams {
    private final String search;
    private final String owner;
    private final Long categoryId;
    private final Integer minRating;
}
