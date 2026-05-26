package com.software.software_development.web.helper;

import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.software.software_development.model.entity.CategoryEntity;
import com.software.software_development.model.entity.ProductEntity;
import com.software.software_development.model.entity.PurchaseEntity;
import com.software.software_development.model.entity.RatingEntity;
import com.software.software_development.model.entity.ReviewEntity;
import com.software.software_development.web.dto.entity.CategoryDto;
import com.software.software_development.web.dto.entity.ProductDto;
import com.software.software_development.web.dto.entity.PurchaseDto;
import com.software.software_development.web.dto.entity.RatingDto;
import com.software.software_development.web.dto.entity.ReviewDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShopDtoMapper {

    private final ModelMapper modelMapper;

    public CategoryDto toCategoryDto(CategoryEntity entity) {
        return modelMapper.map(entity, CategoryDto.class);
    }

    public CategoryEntity toCategoryEntity(CategoryDto dto) {
        return modelMapper.map(dto, CategoryEntity.class);
    }

    public ProductDto toProductDto(ProductEntity entity) {
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setOwnerId(entity.getOwner().getId());
        dto.setOwnerLogin(entity.getOwner().getLogin());
        dto.setAverageRating(Math.round(entity.getAverageRating() * 10.0) / 10.0);
        dto.setReviewCount(entity.getReviewCount());
        List<CategoryDto> categories = entity.getCategories().stream()
                .sorted(Comparator.comparing(CategoryEntity::getName))
                .map(this::toCategoryDto)
                .toList();
        dto.setCategories(categories);
        dto.setCategoryIds(categories.stream().map(CategoryDto::getId).toList());
        return dto;
    }

    public ProductEntity toProductEntity(ProductDto dto) {
        ProductEntity entity = new ProductEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        return entity;
    }

    public ReviewDto toReviewDto(ReviewEntity entity) {
        ReviewDto dto = new ReviewDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setAuthorLogin(entity.getAuthor().getLogin());
        dto.setTitle(entity.getTitle());
        dto.setText(entity.getText());
        dto.setRatingValue(entity.getRating().getValue());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public ReviewEntity toReviewEntity(ReviewDto dto) {
        ReviewEntity entity = new ReviewEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setText(dto.getText());
        return entity;
    }

    public RatingDto toRatingDto(RatingEntity entity) {
        RatingDto dto = new RatingDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setValue(entity.getValue());
        return dto;
    }

    public PurchaseDto toPurchaseDto(PurchaseEntity entity) {
        PurchaseDto dto = new PurchaseDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProduct(toProductDto(entity.getProduct()));
        dto.setBuyerId(entity.getBuyer().getId());
        dto.setBuyerLogin(entity.getBuyer().getLogin());
        dto.setPurchasedAt(entity.getPurchasedAt());
        return dto;
    }
}
