package com.notificationservice.mapper;

import com.notificationservice.dto.KafkaOrderDTO;
import com.notificationservice.dto.ProductKafkaDTO;
import com.notificationservice.entity.OrderEntity;
import com.notificationservice.entity.ProductEntity;
import com.notificationservice.mapping.OrderMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
/**
 * Тесты для {@link OrderMapper}
 *
 * Используется MapStruct для реализации маппера.
 */
public class OrderMapperTest {

    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    /**
     * Проверяет, что метод {@link OrderMapper#toOrderEntity(KafkaOrderDTO)}
     * корректно мапит все нужные поля, включая id товара и пользователя.
     */
    @Test
    void toOrderEntity_mapsFieldsCorrectly() {
        UUID id = UUID.randomUUID();

        KafkaOrderDTO kafkaOrderDTO = KafkaOrderDTO.builder()
                .id(id)
                .userId(3L)
                .build();

        OrderEntity orderEntity = mapper.toOrderEntity(kafkaOrderDTO);

        assertThat(orderEntity.getId()).isEqualTo(kafkaOrderDTO.getId());
        assertThat(orderEntity.getUserId()).isEqualTo(kafkaOrderDTO.getUserId());
    }

    /**
     * Проверяет, что метод {@link OrderMapper#toProductEntity(ProductKafkaDTO)}
     * корректно мапит список товаров, входящий в заказ
     */
    @Test
    void toProductEntity_mapsFieldsCorrectly() {
        UUID id = UUID.randomUUID();

        ProductKafkaDTO productKafkaDTO = ProductKafkaDTO.builder()
                .id(id)
                .name("Футболка")
                .discountedPrice(new BigDecimal("90.00"))
                .totalValueWithDiscount(new BigDecimal("450.00"))
                .quantity(5)
                .sale(new BigDecimal("0.10"))
                .build();

        ProductEntity productEntity = mapper.toProductEntity(productKafkaDTO);

        assertThat(productEntity.getId()).isEqualTo(productKafkaDTO.getId());
        assertThat(productEntity.getName()).isEqualTo(productKafkaDTO.getName());
        assertThat(productEntity.getDiscountedPrice()).isEqualTo(productKafkaDTO.getDiscountedPrice());
        assertThat(productEntity.getTotalValueWithDiscount()).isEqualTo(productKafkaDTO.getTotalValueWithDiscount());
        assertThat(productEntity.getQuantity()).isEqualTo(productKafkaDTO.getQuantity());
        assertThat(productEntity.getSale()).isEqualTo(productKafkaDTO.getSale());
    }

    /**
     * Проверяет, что метод {@link OrderMapper#mapToOrderWithProducts(KafkaOrderDTO)}
     * корректно мапит входящий в заказ список товаров
     */
    @Test
    void mapToOrderWithProducts_mapsProductList() {
        UUID id = UUID.randomUUID();

        KafkaOrderDTO kafkaOrderDTO = KafkaOrderDTO.builder()
                .id(id)
                .build();

        OrderEntity orderEntity = mapper.mapToOrderWithProducts(kafkaOrderDTO);

        assertThat(orderEntity.getProductEntities()).isEqualTo(List.of());
    }

}
