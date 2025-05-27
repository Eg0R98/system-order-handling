package com.notificationservice.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * DTO для передачи данных о заказе через Kafka.
 * Включает информацию о заказе и список товаров в нём.
 */
@Data
public class KafkaOrderDTO {

    /**
     * Уникальный идентификатор заказа.
     */
    private UUID id;

    /**
     * Идентификатор пользователя, который сделал заказ.
     */
    private Long userId;

    /**
     * Список товаров, входящих в заказ.
     */
    private List<ProductKafkaDTO> products;

}
