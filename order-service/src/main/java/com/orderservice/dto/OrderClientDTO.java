package com.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * ДТО-заказ, приходящий от клиента
 */
@Data
@Builder
public class OrderClientDTO {

    /* Список заказанных товаров*/
   private List<ProductClientDTO> products;

}
