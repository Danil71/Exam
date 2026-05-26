package com.software.software_development.service.entity;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewFilterParams {
    private final Long authorId;
    private final String search;
    private final Integer rating;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
}
