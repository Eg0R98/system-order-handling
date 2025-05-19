package com.orderservice.controller;

import com.orderservice.dto.OrderDTOFromClient;
import com.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер отвечает за создание заказа.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Сделать заказ")
public class OrderController {

   private final OrderService orderService;

    /**
     * Создание заказа
     * @param orderDTOFromClient приходит от клиента
     * @return ОTTP-ответ: 200 OK со списком товаров или 400 BAD REQUEST с отсутствующими товарами
     */
    @PostMapping("/api/order")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTOFromClient orderDTOFromClient){

        return orderService.checkProducts(orderDTOFromClient);
    }
}
