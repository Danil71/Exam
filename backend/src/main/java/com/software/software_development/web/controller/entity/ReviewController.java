package com.software.software_development.web.controller.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.utility.SecurityUtils;
import com.software.software_development.service.entity.ReviewFilterParams;
import com.software.software_development.service.entity.ReviewService;
import com.software.software_development.web.dto.entity.ReviewDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;
import com.software.software_development.web.helper.ShopDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.REVIEWS_URL)
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ShopDtoMapper mapper;

    @GetMapping("/my")
    public PageDto<ReviewDto> getMy(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        ReviewFilterParams params = ReviewFilterParams.builder()
                .authorId(SecurityUtils.getCurrentUserId())
                .search(search)
                .rating(rating)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        return PageDtoMapper.toDto(reviewService.getMy(params, page, size), mapper::toReviewDto);
    }

    @GetMapping("/{id}")
    public ReviewDto get(@PathVariable Long id) {
        return mapper.toReviewDto(reviewService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto create(@RequestBody @Valid ReviewDto dto) {
        return mapper.toReviewDto(reviewService.create(
                mapper.toReviewEntity(dto),
                dto.getProductId(),
                SecurityUtils.getCurrentUserId(),
                dto.getRatingValue()
        ));
    }

    @PutMapping("/{id}")
    public ReviewDto update(@PathVariable Long id, @RequestBody @Valid ReviewDto dto) {
        return mapper.toReviewDto(reviewService.update(
                id,
                mapper.toReviewEntity(dto),
                SecurityUtils.getCurrentUserId(),
                dto.getRatingValue()
        ));
    }

    @DeleteMapping("/{id}")
    public ReviewDto delete(@PathVariable Long id) {
        return mapper.toReviewDto(reviewService.delete(id, SecurityUtils.getCurrentUserId()));
    }
}
