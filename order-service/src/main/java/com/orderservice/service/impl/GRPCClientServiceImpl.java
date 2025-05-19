package com.orderservice.service.impl;

import com.orderservice.dto.OrderDTOFromClient;
import com.orderservice.service.GRPCClientService;
import inventory.InventoryServiceGrpc;
import inventory.Product.ProductDTOFromOrderService;
import inventory.Product.ProductsRequest;
import inventory.Product.ProductsResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * Реализация клиента для обращения к inventory-сервису по gRPC-протоколу.
 */
@Service
public class GRPCClientServiceImpl implements GRPCClientService {

    /**
     * gRPC-заглушка для обращения к inventory-сервису.
     */
    @GrpcClient("inventory-service")
    private InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub;

    /**
     * Формирует запрос на основе товаров из заказа клиента и отправляет его
     * в inventory-сервис для проверки наличия на складе.
     *
     * @param orderDTOFromClient заказ, полученный от клиента
     * @return ответ inventory-сервиса с результатом проверки
     */
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
