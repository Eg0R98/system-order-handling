package com.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductDTOFoKafka {

    private UUID id;

    private String name;

    private BigDecimal discountedPrice;

    private BigDecimal totalValueWithDiscount;

    private Integer quantity;

    private BigDecimal sale;


}
