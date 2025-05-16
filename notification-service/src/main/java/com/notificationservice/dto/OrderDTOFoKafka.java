package com.notificationservice.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class OrderDTOFoKafka {

    private UUID id;

    private Long userId;

    private List<ProductDTOFoKafka> products;

}
