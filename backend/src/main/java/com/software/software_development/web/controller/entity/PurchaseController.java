package com.software.software_development.web.controller.entity;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.utility.SecurityUtils;
import com.software.software_development.service.entity.PurchaseService;
import com.software.software_development.web.dto.entity.PurchaseDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;
import com.software.software_development.web.helper.ShopDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.PURCHASES_URL)
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final ShopDtoMapper mapper;

    @GetMapping("/my")
    public PageDto<PurchaseDto> getMy(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        return PageDtoMapper.toDto(
                purchaseService.getMy(SecurityUtils.getCurrentUserId(), page, size),
                mapper::toPurchaseDto
        );
    }

    @GetMapping("/my/products/{productId}")
    public Map<String, Boolean> isPurchased(@PathVariable Long productId) {
        boolean purchased = purchaseService.hasPurchased(productId, SecurityUtils.getCurrentUserId());
        return Map.of("purchased", purchased);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseDto buy(@RequestBody @Valid PurchaseDto dto) {
        return mapper.toPurchaseDto(purchaseService.buy(dto.getProductId(), SecurityUtils.getCurrentUserId()));
    }
}
