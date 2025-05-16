package com.orderservice.service;

import com.orderservice.dto.OrderDTOFoKafka;

public interface KafkaProducerService {
    void sendOrderToKafka(OrderDTOFoKafka order);
}
