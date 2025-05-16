package com.orderservice.mapping;

import com.orderservice.dto.ProductDTOFoKafka;
import inventory.Product.SuccessfulProductDTOFromInventoryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "productId", target = "id", qualifiedByName = "mapProductId")
    @Mapping(source = "discountedPrice", target = "discountedPrice", qualifiedByName = "mapBigDecimal")
    @Mapping(source = "totalValueWithDiscount", target = "totalValueWithDiscount", qualifiedByName = "mapBigDecimal")
    @Mapping(source = "sale", target = "sale", qualifiedByName = "mapBigDecimal")
    ProductDTOFoKafka toKafkaProduct(SuccessfulProductDTOFromInventoryService grpcDto);

    List<ProductDTOFoKafka> toKafkaProductList(List<SuccessfulProductDTOFromInventoryService> products);

    @Named("mapProductId")
    default UUID mapProductId(String id) {
        return UUID.fromString(id);
    }

    @Named("mapBigDecimal")
    default BigDecimal mapBigDecimal(String value) {
        return new BigDecimal(value);
    }

}
