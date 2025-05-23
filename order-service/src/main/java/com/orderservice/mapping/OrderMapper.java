package com.orderservice.mapping;

import com.orderservice.dto.OrderClientDTO;
import com.orderservice.dto.OrderKafkaDTO;
import com.orderservice.dto.ProductKafkaDTO;
import inventory.Product.ProductOrderServiceDTO;
import inventory.Product.ProductsRequest;
import inventory.Product.SuccessfulProductInventoryServiceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Маппер для преобразования различных типов ДТО
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Метод преобразует список ДТО-товар принятых из InventoryService с помощью GRPC в ДТО-заказ
     * id для ДТО-заказа устанавливается отдельно
     */
    default OrderKafkaDTO toKafkaOrder(List<SuccessfulProductInventoryServiceDTO> products, Long userId){
        OrderKafkaDTO orderKafkaDTO = new OrderKafkaDTO();
        orderKafkaDTO.setUserId(userId);
        List<ProductKafkaDTO> kafkaProductList = toKafkaProductList(products);
        orderKafkaDTO.setProducts(kafkaProductList);
        return orderKafkaDTO;
    }

    /**
     * Метод преобразует ДТО-товар принятый от клиента в ДТО,
     * отправляемый в inventory-service с помощью GRPC
     */
    default ProductsRequest toProductResponse(OrderClientDTO orderClientDTO){
        return ProductsRequest.newBuilder().addAllProducts(
                orderClientDTO.getProducts().stream().map(productDTO ->
                                ProductOrderServiceDTO.newBuilder()
                                        .setProductId(String.valueOf(productDTO.getId()))
                                        .setQuantity(productDTO.getQuantity()).build())
                        .toList()).build();
    }

    /**
     * Метод преобразует ДТО-товар принятый из InventoryService с помощью GRPC в ДТО-товар, отправляемый в кафку
     * поля id, discountedPrice, totalValueWithDiscount и sale маппятся отдельно, чтобы избежать ошибок
     */
    @Mapping(source = "productId", target = "id", qualifiedByName = "mapProductId")
    @Mapping(source = "discountedPrice", target = "discountedPrice", qualifiedByName = "mapBigDecimal")
    @Mapping(source = "totalValueWithDiscount", target = "totalValueWithDiscount", qualifiedByName = "mapBigDecimal")
    @Mapping(source = "sale", target = "sale", qualifiedByName = "mapBigDecimal")
    ProductKafkaDTO toKafkaProduct(SuccessfulProductInventoryServiceDTO grpcDto);

    /* Маппинг списка ДТО-товаров*/
    List<ProductKafkaDTO> toKafkaProductList(List<SuccessfulProductInventoryServiceDTO> products);

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
