package com.software.software_development.web.dto.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Min(1)
    private Long productId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProductDto product;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long buyerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String buyerLogin;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime purchasedAt;
}
