package com.inditex.controller;

import com.inditex.controller.dto.PricesOutDto;
import com.inditex.controller.dto.PricesSpecification;
import com.inditex.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1")
public class PricesController {

    private final ProductService productService;

    @GetMapping("/prices")
    public ResponseEntity<PricesOutDto> getPrices(PricesSpecification criteria) {
        return ResponseEntity.ok(productService.getProductInfo(criteria));

    }


}
