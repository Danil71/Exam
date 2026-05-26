package com.software.software_development.web.controller.entity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.service.entity.ProductFilterParams;
import com.software.software_development.service.entity.ProductService;
import com.software.software_development.service.entity.ReviewService;
import com.software.software_development.web.dto.entity.ProductDto;
import com.software.software_development.web.dto.entity.ReviewDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;
import com.software.software_development.web.helper.ShopDtoMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.PRODUCTS_URL)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final ShopDtoMapper mapper;

    @GetMapping("/public")
    public PageDto<ProductDto> getPublic(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        ProductFilterParams params = ProductFilterParams.builder()
                .search(search)
                .owner(owner)
                .categoryId(categoryId)
                .minRating(minRating)
                .build();
        return PageDtoMapper.toDto(productService.getAll(params, page, size), mapper::toProductDto);
    }

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable Long id) {
        return mapper.toProductDto(productService.get(id));
    }

    @GetMapping("/{productId}/reviews")
    public PageDto<ReviewDto> getReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        return PageDtoMapper.toDto(reviewService.getByProduct(productId, page, size), mapper::toReviewDto);
    }
}
