package com.orderservice.dto;

import lombok.Data;

import java.util.List;


@Data
public class OrderDTOFromClient {

    List<ProductDTOFromClient> products;

}
