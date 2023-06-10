package com.inditex.controller.dto;

import java.time.LocalDateTime;

public record PricesInDto(Integer brandId,
                          LocalDateTime applicationDate,
                          Integer productId) {

}
