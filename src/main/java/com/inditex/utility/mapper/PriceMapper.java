package com.inditex.utility.mapper;

import com.inditex.controller.dto.PricesOutDto;
import com.inditex.domain.Prices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PriceMapper {

    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "priceList.id", target = "appliedTariff")
    PricesOutDto entityToDto(Prices prices);
}
