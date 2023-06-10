package com.inditex.service;

import com.inditex.controller.dto.PricesInDto;
import com.inditex.controller.dto.PricesOutDto;

public interface ProductService {

    PricesOutDto getProductInfo(PricesInDto pricesDto);
}
