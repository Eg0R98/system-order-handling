package com.inventoryservice.mapper;

import com.inventoryservice.entity.ProductEntity;
import inventory.Product.ProductOrderServiceDTO;
import inventory.Product.SuccessfulProductInventoryServiceDTO;
import inventory.Product.UnsuccessfulProductInventoryServiceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Маппер преобразует сущности, в ДТО, которое отправляется клиенту по GRPC
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    /**
     * Преобразует сущность {@link ProductEntity} в DTO {@link SuccessfulProductInventoryServiceDTO},
     * используемое при успешной обработке запроса order-service.
     *
     * @param product сущность продукта из базы данных
     * @return DTO для успешного ответа order-service
     */
    @Mapping(target = "productId", source = "id")
    @Mapping(target = "discountedPrice", expression = "java(String.valueOf(product.getDiscountedPrice()))")
    @Mapping(target = "totalValueWithDiscount", expression = "java(String.valueOf(product.getTotalValueWithDiscount()))")
    @Mapping(target = "sale", expression = "java(String.valueOf(product.getSale()))")
    SuccessfulProductInventoryServiceDTO toSuccessfulDTO(ProductEntity product);

    /**
     * Преобразует DTO заказа {@link ProductOrderServiceDTO} в DTO {@link UnsuccessfulProductInventoryServiceDTO},
     * используемое при неудачной обработке запроса order-service.
     *
     * @param product DTO, содержащий информацию о заказе продукта
     * @return DTO для отрицательного ответа order-service
     */
    default UnsuccessfulProductInventoryServiceDTO toUnsuccessfulDTO(ProductOrderServiceDTO product) {
        return UnsuccessfulProductInventoryServiceDTO.newBuilder()
                .setName(product.getName())
                .build();
    }
}
