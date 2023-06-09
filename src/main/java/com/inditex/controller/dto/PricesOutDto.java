package com.inditex.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PricesOutDto(Integer brandId,
                           Integer productId,
                           Integer appliedTariff,
                           LocalDateTime startDate,
                           LocalDateTime endDate,
                           BigDecimal price) {
}
