package com.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * ДТО-товар, который присылает клиент
 */
@Data
@Builder
public class ProductClientDTO{

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
