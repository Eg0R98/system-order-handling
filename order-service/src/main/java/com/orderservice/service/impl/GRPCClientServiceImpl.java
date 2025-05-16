package com.orderservice.service.impl;

import com.orderservice.dto.OrderDTOFromClient;
import com.orderservice.service.GRPCClientService;
import inventory.InventoryServiceGrpc;
import inventory.Product.ProductDTOFromOrderService;
import inventory.Product.ProductsRequest;
import inventory.Product.ProductsResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GRPCClientServiceImpl implements GRPCClientService {

    @GrpcClient("inventory-service")
    private InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub;

    @Override
    public ProductsResponse checkAvailability(OrderDTOFromClient orderDTOFromClient) {
        ProductsRequest productsRequest = ProductsRequest.newBuilder().addAllProducts(
                orderDTOFromClient.getProducts().stream().map(productDTO ->
                                ProductDTOFromOrderService.newBuilder()
                                        .setProductId(String.valueOf(productDTO.getId()))
                                        .setQuantity(productDTO.getQuantity()).build())
                        .toList()).build();

        return inventoryStub.checkAvailability(productsRequest);

    }
}
