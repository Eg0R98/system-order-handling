package com.orderservice.service;

import com.orderservice.dto.OrderKafkaDTO;

/**
 * Сервис для отправки заказов в Kafka.
 */
public interface KafkaProducerService {

    /**
     * Отправляет заказ в Kafka.
     *
     * @param order ДТО заказа для Kafka
     */
    void sendOrderToKafka(OrderKafkaDTO order);
}
