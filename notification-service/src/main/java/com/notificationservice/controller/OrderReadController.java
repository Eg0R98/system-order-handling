package com.notificationservice.controller;

import com.notificationservice.entity.OrderEntity;
import com.notificationservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST-контроллер для получения информации о заказах.
 */
@RestController
@RequestMapping("/api/orders/")
@RequiredArgsConstructor
@Tag(name = "Получение заказов")
public class OrderReadController {

    private final OrderService service;

    /**
     * Получение списка всех заказов.
     *
     * @return HTTP-ответ с перечнем всех заказов и статусом 200 OK
     */
    @Operation(summary = "Получить все заказы")
    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAll() {
        List<OrderEntity> orderEntities = service.findAll();

        return ResponseEntity.ok(orderEntities);
    }

    /**
     * Получение заказа по его уникальному идентификатору.
     *
     * @param orderId UUID заказа
     * @return HTTP-ответ с заказом и статусом 200 OK
     */
    @Operation(summary = "Получить заказ по его id")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderEntity> getByOrderId(@PathVariable UUID orderId) {
        OrderEntity orderEntity = service.findByOrderId(orderId);

        return ResponseEntity.ok(orderEntity);
    }

    /**
     * Получение списка заказов по id пользователя.
     *
     * @param userId пользователя
     * @return HTTP-ответ со списком заказов пользователя и статусом 200 OK
     */
    @Operation(summary = "Получить заказы по id пользователя")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderEntity>> getByUserId(@PathVariable Long userId) {
        List<OrderEntity> orderEntities = service.findByUserId(userId);

        return ResponseEntity.ok(orderEntities);
    }



}
