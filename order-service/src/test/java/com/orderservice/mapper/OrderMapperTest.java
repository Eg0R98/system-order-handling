package com.orderservice.mapper;

import com.orderservice.dto.OrderClientDTO;
import com.orderservice.dto.ProductClientDTO;
import com.orderservice.dto.ProductKafkaDTO;
import com.orderservice.mapping.OrderMapper;
import inventory.Product.ProductOrderServiceDTO;
import inventory.Product.ProductsRequest;
import inventory.Product.SuccessfulProductInventoryServiceDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для {@link OrderMapper}
 *
 * Используется MapStruct для реализации маппера.
 */
public class OrderMapperTest {

    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    /**
     * Проверяет, что метод {@link OrderMapper#toProductResponse(OrderClientDTO)}
     * корректно мапит все нужные поля, а именно id и количество товара, который входит в заказ
     */
    @Test
    void toProductResponse_mapFieldsCorrectly(){
        UUID id = UUID.randomUUID();

        ProductClientDTO productClientDTO = ProductClientDTO.builder()
                .id(id)
                .quantity(5)
                .build();

        OrderClientDTO orderClientDTO = OrderClientDTO.builder().products(List.of(productClientDTO)).build();

        ProductsRequest productsRequest = mapper.toProductResponse(orderClientDTO);

        ProductOrderServiceDTO requestProduct = productsRequest.getProducts(0);

        assertThat(requestProduct.getProductId()).isEqualTo(productClientDTO.getId().toString());
        assertThat(requestProduct.getQuantity()).isEqualTo(productClientDTO.getQuantity());
    }

    /**
     * Проверяет, что метод {@link OrderMapper#toKafkaProductList(List<ProductKafkaDTO>)}
     * корректно мапит список товаров
     */
    @Test
    void toKafkaProductList_mapFieldsCorrectly(){
        UUID id = UUID.randomUUID();
        SuccessfulProductInventoryServiceDTO serviceDTO = SuccessfulProductInventoryServiceDTO.newBuilder()
                .setProductId(id.toString())
                .setName("Футболка")
                .setDiscountedPrice("90.00")
                .setTotalValueWithDiscount("450.00")
                .setQuantity(5)
                .setSale("0.10")
                .build();

        List<ProductKafkaDTO> kafkaList = mapper.toKafkaProductList(List.of(serviceDTO));

        assertThat(kafkaList).hasSize(1);

        ProductKafkaDTO kafkaProduct = kafkaList.getFirst();

        assertThat(kafkaProduct.getId().toString()).isEqualTo(serviceDTO.getProductId());
        assertThat(kafkaProduct.getName()).isEqualTo(serviceDTO.getName());
        assertThat(kafkaProduct.getDiscountedPrice()).isEqualTo(serviceDTO.getDiscountedPrice());
        assertThat(kafkaProduct.getTotalValueWithDiscount()).isEqualTo(serviceDTO.getTotalValueWithDiscount());
        assertThat(kafkaProduct.getQuantity()).isEqualTo(serviceDTO.getQuantity());
        assertThat(kafkaProduct.getSale()).isEqualTo(serviceDTO.getSale());
    }
    /**
     * Проверяет, что метод {@link OrderMapper#toKafkaProduct(SuccessfulProductInventoryServiceDTO)}
     * корректно мапит все нужные поля, а именно id, имя,
     * цену со скидкой, полную стоимость, количество, скидку
     */
    @Test
    void toKafkaProduct_mapFieldsCorrectly(){
        UUID id = UUID.randomUUID();
        SuccessfulProductInventoryServiceDTO serviceDTO = SuccessfulProductInventoryServiceDTO.newBuilder()
                .setProductId(id.toString())
                .setName("Футболка")
                .setDiscountedPrice("90.00")
                .setTotalValueWithDiscount("450.00")
                .setQuantity(5)
                .setSale("0.10")
                .build();

        ProductKafkaDTO kafkaProduct = mapper.toKafkaProduct(serviceDTO);

        assertThat(kafkaProduct.getId().toString()).isEqualTo(serviceDTO.getProductId());
        assertThat(kafkaProduct.getName()).isEqualTo(serviceDTO.getName());
        assertThat(kafkaProduct.getDiscountedPrice()).isEqualTo(serviceDTO.getDiscountedPrice());
        assertThat(kafkaProduct.getTotalValueWithDiscount()).isEqualTo(serviceDTO.getTotalValueWithDiscount());
        assertThat(kafkaProduct.getQuantity()).isEqualTo(serviceDTO.getQuantity());
        assertThat(kafkaProduct.getSale()).isEqualTo(serviceDTO.getSale());
    }

}
