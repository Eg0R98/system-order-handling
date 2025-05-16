package com.orderservice.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class OrderDTOFoKafka {

    private UUID id = UUID.randomUUID();

    private Long userId;

    private List<ProductDTOFoKafka> products;

}
