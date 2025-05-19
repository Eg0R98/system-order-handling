package com.orderservice.service.impl;

import com.orderservice.dto.OrderDTOFoKafka;
import com.orderservice.dto.OrderDTOFromClient;
import com.orderservice.dto.ProductDTOFoKafka;
import com.orderservice.mapping.ProductMapper;
import com.orderservice.service.GRPCClientService;
import com.orderservice.service.KafkaProducerService;
import com.orderservice.service.OrderService;
import com.orderservice.service.UserService;
import inventory.Product.SuccessfulProductDTOFromInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Реализация сервиса для обработки заказов.
 * Проверяет наличие товаров, отправляет данные заказа в Kafka,
 * взаимодействует с внешними сервисами (gRPC и Kafka).
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final GRPCClientService grpcClientService;

    private final KafkaProducerService kafkaProducerService;

    private final UserService userService;

    private final ProductMapper mapper;

    /**
     * Проверяет наличие товаров на складе, переданных от клиента.
     * Если часть товаров недоступна, возвращает ошибку 400 с их списком.
     * Если все товары доступны, отправляет заказ в Kafka и возвращает список заказанных товаров.
     *
     * @param orderDTOFromClient заказ, полученный от клиента
     * @return HTTP-ответ: 200 OK со списком товаров или 400 BAD REQUEST с отсутствующими товарами
     */
    @Override
    public ResponseEntity<?> checkProducts(OrderDTOFromClient orderDTOFromClient) {

        // Проверка доступности товаров через gRPC-сервис
        var productsResponse = grpcClientService.checkAvailability(orderDTOFromClient);

        // Если есть недоступные товары, вернуть ошибку
        if (!productsResponse.getUnsuccessfulProductsList().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Некоторые товары отсутствуют", "unavailable", productsResponse.getUnsuccessfulProductsList()));
        }

        // Создание DTO для отправки в Kafka
        OrderDTOFoKafka orderDTOFoKafka = new OrderDTOFoKafka();
        orderDTOFoKafka.setUserId(userService.getCurrentUserId());

        List<SuccessfulProductDTOFromInventoryService> successfulProductsList = productsResponse.getSuccessfulProductsList();

        // Преобразование товаров в формат для Kafka
        List<ProductDTOFoKafka> kafkaProductList = mapper.toKafkaProductList(successfulProductsList);
        orderDTOFoKafka.setProducts(kafkaProductList);

        // Отправка заказа в Kafka
        kafkaProducerService.sendOrderToKafka(orderDTOFoKafka);

        return ResponseEntity.ok(kafkaProductList);
    }


}
