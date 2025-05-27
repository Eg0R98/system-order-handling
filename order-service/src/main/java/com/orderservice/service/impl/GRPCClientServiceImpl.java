package com.orderservice.service.impl;

import com.orderservice.dto.OrderClientDTO;
import com.orderservice.mapping.OrderMapper;
import com.orderservice.service.GRPCClientService;
import inventory.InventoryServiceGrpc;
import inventory.Product.ProductsRequest;
import inventory.Product.ProductsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * Реализация клиента для обращения к inventory-сервису по gRPC-протоколу.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GRPCClientServiceImpl implements GRPCClientService {

    private final OrderMapper mapper;

    /**
     * gRPC-заглушка для обращения к inventory-сервису.
     */
    @GrpcClient("inventory-service")
    private InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub;

    /**
     * Формирует запрос на основе товаров из заказа клиента и отправляет его
     * в inventory-сервис для проверки наличия на складе.
     *
     * @param orderClientDTO заказ, полученный от клиента
     * @return ответ inventory-сервиса с результатом проверки
     */
    @Override
    public ProductsResponse checkAvailability(OrderClientDTO orderClientDTO) {

        ProductsRequest productsRequest = mapper.toProductResponse(orderClientDTO);

        log.info("Grpc-запрос {} отправлен в inventory-service", productsRequest);
        return inventoryStub.checkAvailability(productsRequest);

    }
}
