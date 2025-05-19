package com.orderservice.service;

import com.orderservice.dto.OrderDTOFromClient;
import inventory.Product.ProductsResponse;

/**
 * Сервис для взаимодействия с внешним инвентарным сервисом по GRPC.
 */
public interface GRPCClientService {

    /**
     * Проверяет наличие товаров на складе через gRPC-запрос к inventory-сервису.
     *
     * @param orderDTOFromClient объект заказа, содержащий список товаров и их количество
     * @return ответ от inventory-сервиса с информацией о доступных и недоступных товарах
     */
    ProductsResponse checkAvailability(OrderDTOFromClient orderDTOFromClient);
}
