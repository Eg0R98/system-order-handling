package com.orderservice.service.impl;

import com.orderservice.dto.OrderClientDTO;
import com.orderservice.dto.OrderKafkaDTO;
import com.orderservice.exception.NotRequiredProductException;
import com.orderservice.mapping.OrderMapper;
import com.orderservice.service.GRPCClientService;
import com.orderservice.service.KafkaProducerService;
import com.orderservice.service.OrderService;
import com.orderservice.service.UserService;
import inventory.Product.ProductsResponse;
import inventory.Product.SuccessfulProductInventoryServiceDTO;
import inventory.Product.UnsuccessfulProductInventoryServiceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для обработки заказов.
 * Проверяет наличие товаров, отправляет данные заказа в Kafka,
 * взаимодействует с внешними сервисами (gRPC и Kafka).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final GRPCClientService grpcClientService;

    private final KafkaProducerService kafkaProducerService;

    private final UserService userService;

    private final OrderMapper mapper;

    /**
     * Проверяет наличие товаров на складе, переданных от клиента.
     * Если часть товаров недоступна, возвращает ошибку 400 с их списком.
     * Если все товары доступны, отправляет заказ в Kafka и возвращает список заказанных товаров.
     *
     * @param orderClientDTO заказ, полученный от клиента
     * @return HTTP-ответ: 200 OK со списком товаров или 400 BAD REQUEST с отсутствующими товарами
     */
    @Override
    public ResponseEntity<OrderKafkaDTO> checkProducts(OrderClientDTO orderClientDTO) {

        // Проверка доступности товаров через gRPC-сервис
        ProductsResponse productsResponse = grpcClientService.checkAvailability(orderClientDTO);

        log.info("Получен Grpc-ответ {}", productsResponse);

        // Если есть недоступные товары, выбрасываем исключение
        if (!productsResponse.getUnsuccessfulProductsList().isEmpty()) {
            List<UnsuccessfulProductInventoryServiceDTO> unsuccessfulProductsList = productsResponse.getUnsuccessfulProductsList();
            throw new NotRequiredProductException("Некоторые товары отсутствуют",
                    unsuccessfulProductsList.stream()
                            .map(UnsuccessfulProductInventoryServiceDTO::getName)
                            .collect(Collectors.toList()));

        }

        // Создание DTO для отправки в Kafka
        List<SuccessfulProductInventoryServiceDTO> successfulProductsList = productsResponse.getSuccessfulProductsList();

        // Преобразование товаров в формат для Kafka
        OrderKafkaDTO orderKafkaDTO = mapper.toKafkaOrder(successfulProductsList, userService.getCurrentUserId());

        // Отправка заказа в Kafka
        kafkaProducerService.sendOrderToKafka(orderKafkaDTO);

        return ResponseEntity.ok(orderKafkaDTO);
    }


}
