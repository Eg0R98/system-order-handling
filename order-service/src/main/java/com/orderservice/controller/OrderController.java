package com.orderservice.controller;

import com.orderservice.dto.OrderDTOFromClient;
import com.orderservice.service.impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Сделать заказ")
public class OrderController {

   private final OrderServiceImpl orderService;

    @PostMapping("/api/order")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTOFromClient orderDTOFromClient){

        return orderService.checkProducts(orderDTOFromClient);
    }
}
