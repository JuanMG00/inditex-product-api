package com.inditex.service;

import com.inditex.controller.dto.PricesOutDto;
import com.inditex.controller.dto.PricesInDto;
import com.inditex.domain.Prices;
import com.inditex.repository.PricesRepository;
import com.inditex.utility.mapper.PriceMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final PricesRepository pricesRepository;
    private final PriceMapper mapper;

    @Override
    public PricesOutDto getProductInfo(PricesInDto dto) {
        Prices price = pricesRepository.findMatchingPrice(dto.productId(), dto.brandId(), dto.applicationDate())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("There is not any price set for the date %s", dto.applicationDate())));
        return mapper.entityToDto(price);
    }
}
