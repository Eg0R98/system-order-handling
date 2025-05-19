package com.orderservice.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * ДТО-заказ, отправляемый в кафку
 */
@Data
public class OrderDTOFoKafka {

    /*id заказа. Генерируется при отправке*/
    private UUID id = UUID.randomUUID();

    /*id пользователя, который сделал заказ*/
    private Long userId;

    /* Список заказанных товаров*/
    private List<ProductDTOFoKafka> products;

}
