package com.orderservice.dto;

import lombok.Data;

import java.util.List;

/**
 * ДТО-заказ, приходящий от клиента
 */
@Data
public class OrderDTOFromClient {


    /* Список заказанных товаров*/
    List<ProductDTOFromClient> products;

}
