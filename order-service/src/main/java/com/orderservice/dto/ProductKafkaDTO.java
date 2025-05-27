package com.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * ДТО-товар, отправляемый в кафку
 */
@Data
public class ProductKafkaDTO {

    /*id товара*/
    private UUID id;

    /*Имя товара*/
    private String name;

    /*Цена с учетом скидки*/
    private BigDecimal discountedPrice;

    /*Общая стоимость товаров, учитывая скидку и их количество*/
    private BigDecimal totalValueWithDiscount;

    /*количество экземпляров товара*/
    private Integer quantity;

    /*Скидка в виде десятичной дроби*/
    private BigDecimal sale;


}
