package com.orderservice.dto;

import lombok.Data;

import java.util.List;

/**
 * ДТО-заказ, приходящий от клиента
 */
@Data
public class OrderClientDTO {

    /* Список заказанных товаров*/
    List<ProductClientDTO> products;

}
