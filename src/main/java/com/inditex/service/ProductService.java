package com.inditex.service;

import com.inditex.controller.dto.PricesSpecification;
import com.inditex.controller.dto.PricesOutDto;

public interface ProductService {

    PricesOutDto getProductInfo(PricesSpecification pricesDto);
}
