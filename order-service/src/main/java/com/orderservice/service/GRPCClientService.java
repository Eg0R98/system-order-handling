package com.orderservice.service;

import com.orderservice.dto.OrderDTOFromClient;
import inventory.Product.ProductsResponse;

public interface GRPCClientService {
    ProductsResponse checkAvailability(OrderDTOFromClient orderDTOFromClient);
}
