package com.notificationservice.controller;

import com.notificationservice.entity.Order;
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

@RestController
@RequestMapping("/api/orders/")
@RequiredArgsConstructor
@Tag(name = "Получение заказов")
public class OrderReadController {

    private final OrderService service;

    @Operation(summary = "Получить все заказы")
    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        List<Order> orders = service.findAll();

        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Получить заказ по его id")
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getByOrderId(@PathVariable UUID orderId) {
        Order order = service.findByOrderId(orderId);

        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Получить заказы по id пользователя")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getByUserId(@PathVariable Long userId) {
        List<Order> orders = service.findByUserId(userId);

        return ResponseEntity.ok(orders);
    }



}
