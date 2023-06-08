package com.inditex.controller.dto;

import java.time.LocalDateTime;

public record PricesOutDto(Integer brandId, LocalDateTime applicationDate,
                           Integer productId) {
}
