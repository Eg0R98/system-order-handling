package com.notificationservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO для передачи данных о товаре через Kafka.
 * Содержит основные поля товара, включая скидку и итоговые значения.
 */
@Data
public class ProductKafkaDTO {
    /**
     * Уникальный идентификатор товара.
     */
    private UUID id;

    /**
     * Название товара.
     */
    private String name;

    /**
     * Цена товара с учётом скидки.
     */
    private BigDecimal discountedPrice;

    /**
     * Общая стоимость товара с учётом количества и скидки.
     */
    private BigDecimal totalValueWithDiscount;

    /**
     * Количество товара в заказе.
     */
    private Integer quantity;

    /**
     * Скидка на товар в виде десятичной дроби (например, 0.10 = 10%).
     */
    private BigDecimal sale;
}
