package com.orderservice.dto;

import lombok.Data;

import java.util.UUID;

/**
 * ДТО-товар, который присылает клиент
 */
@Data
public class ProductDTOFromClient{

    /* id товара*/
    private UUID id;

    /* Количество экземпляров товара*/
    private Integer quantity;

}
