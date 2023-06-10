package com.inditex.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PricesInDto(@NotNull(message = "brandId cannot be null") Integer brandId,
                          @NotNull(message = "applicationDate cannot be null") LocalDateTime applicationDate,
                          @NotNull(message = "productId cannot be null") Integer productId) {

}
