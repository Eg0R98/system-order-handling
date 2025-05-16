package com.orderservice.service.impl;

import com.orderservice.dto.OrderDTOFoKafka;
import com.orderservice.dto.OrderDTOFromClient;
import com.orderservice.dto.ProductDTOFoKafka;
import com.orderservice.mapping.OrderMapper;
import com.orderservice.service.GRPCClientService;
import com.orderservice.service.KafkaProducerService;
import com.orderservice.service.UserService;
import inventory.Product.SuccessfulProductDTOFromInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {

    private final GRPCClientService grpcClientService;

    private final KafkaProducerService kafkaProducerService;

    private final UserService userService;

    private final OrderMapper mapper;

    public ResponseEntity<?> checkProducts(OrderDTOFromClient orderDTOFromClient) {

        var productsResponse = grpcClientService.checkAvailability(orderDTOFromClient);

        if (!productsResponse.getUnsuccessfulProductsList().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Некоторые товары отсутствуют", "unavailable", productsResponse.getUnsuccessfulProductsList()));
        }

        OrderDTOFoKafka orderDTOFoKafka = new OrderDTOFoKafka();
        orderDTOFoKafka.setUserId(userService.getCurrentUserId());

        List<SuccessfulProductDTOFromInventoryService> successfulProductsList = productsResponse.getSuccessfulProductsList();

        List<ProductDTOFoKafka> kafkaProductList = mapper.toKafkaProductList(successfulProductsList);

        orderDTOFoKafka.setProducts(kafkaProductList);

        kafkaProducerService.sendOrderToKafka(orderDTOFoKafka);

        return ResponseEntity.ok(kafkaProductList);
    }


}
