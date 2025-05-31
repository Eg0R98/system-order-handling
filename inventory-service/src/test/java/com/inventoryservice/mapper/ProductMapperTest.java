package com.inventoryservice.mapper;

import com.inventoryservice.entity.ProductEntity;
import inventory.Product.ProductOrderServiceDTO;
import inventory.Product.SuccessfulProductInventoryServiceDTO;
import inventory.Product.UnsuccessfulProductInventoryServiceDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Тесты для {@link ProductMapper} — интерфейса маппера, который преобразует
 * сущности {@link ProductEntity} в DTO для gRPC сервиса и наоборот.
 *
 * Используется MapStruct для реализации маппера.
 */
public class ProductMapperTest {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    /**
     * Проверяет, что метод {@link ProductMapper#toSuccessfulDTO(ProductEntity)}
     * корректно мапит все нужные поля, включая id и вычисляемые значения цены с учетом скидки.
     */
    @Test
    void toSuccessfulDTO_mapsFieldsCorrectly() {
        UUID id = UUID.randomUUID();

        ProductEntity product = ProductEntity.builder()
                .id(id)
                .name("Футболка")
                .price(new BigDecimal("100"))
                .sale(new BigDecimal("0.10"))
                .quantity(5)
                .build();

        SuccessfulProductInventoryServiceDTO dto = mapper.toSuccessfulDTO(product);

        assertThat(dto.getProductId()).isEqualTo(id.toString());
        assertThat(dto.getDiscountedPrice()).isEqualTo("90.00");
        assertThat(dto.getTotalValueWithDiscount()).isEqualTo("450.00");
        assertThat(dto.getSale()).isEqualTo("0.10");
    }

    /**
     * Проверяет, что метод {@link ProductMapper#toUnsuccessfulDTO(ProductOrderServiceDTO)}
     * корректно преобразует поле name из DTO заказа в DTO для неуспешного ответа.
     */
    @Test
    void toUnsuccessfulDTO_mapsNameCorrectly() {
        ProductOrderServiceDTO orderDto = ProductOrderServiceDTO.newBuilder()
                .setName("Test Product")
                .build();

        UnsuccessfulProductInventoryServiceDTO dto = mapper.toUnsuccessfulDTO(orderDto);

        assertThat(dto.getName()).isEqualTo("Test Product");
    }
}
