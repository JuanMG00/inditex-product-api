package com.inditex.service;

import com.inditex.controller.dto.PricesOutDto;
import com.inditex.controller.dto.PricesSpecification;
import com.inditex.repository.PricesRepository;
import com.inditex.utility.mapper.PriceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final PricesRepository pricesRepository;
    private final PriceMapper mapper;

    @Override
    public PricesOutDto getProductInfo(PricesSpecification specification) {
        return mapper.entityToDto(pricesRepository.findAll(specification).stream()
                .findFirst().orElse(null));
    }
}
