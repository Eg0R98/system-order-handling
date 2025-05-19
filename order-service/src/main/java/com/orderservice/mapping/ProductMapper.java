package com.orderservice.mapping;

import com.orderservice.dto.ProductDTOFoKafka;
import inventory.Product.SuccessfulProductDTOFromInventoryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Маппер преобразует ДТО, пришедшее по GRPC из InventoryService, в ДТО, которое отправляется в кафку
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Метод преобразует ДТО-товар принятый из InventoryService с помощью GRPC в ДТО-товар, отправляемый в кафку
     * поля id, discountedPrice, totalValueWithDiscount и sale маппятся отдельно, чтобы избежать ошибок
     */
    @Mapping(source = "productId", target = "id", qualifiedByName = "mapProductId")
    @Mapping(source = "discountedPrice", target = "discountedPrice", qualifiedByName = "mapBigDecimal")
    @Mapping(source = "totalValueWithDiscount", target = "totalValueWithDiscount", qualifiedByName = "mapBigDecimal")
    @Mapping(source = "sale", target = "sale", qualifiedByName = "mapBigDecimal")
    ProductDTOFoKafka toKafkaProduct(SuccessfulProductDTOFromInventoryService grpcDto);

    /* Маппинг списка ДТО-товаров*/
    List<ProductDTOFoKafka> toKafkaProductList(List<SuccessfulProductDTOFromInventoryService> products);

    /* Маппинг id из строкового представления в UUID*/
    @Named("mapProductId")
    default UUID mapProductId(String id) {
        return UUID.fromString(id);
    }

    /* Маппинг BigDecimal из строкового представления в класс BigDecimal*/
    @Named("mapBigDecimal")
    default BigDecimal mapBigDecimal(String value) {
        return new BigDecimal(value);
    }

}
