package com.orderservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDTOFromClient{

    private UUID id;

    private Integer quantity;

}
