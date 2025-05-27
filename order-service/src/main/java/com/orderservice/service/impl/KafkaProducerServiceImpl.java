package com.orderservice.service.impl;

import com.orderservice.dto.OrderKafkaDTO;
import com.orderservice.exception.KafkaDeliveryException;
import com.orderservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * Реализация сервиса {@link KafkaProducerService} для отправки сообщений в Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, OrderKafkaDTO> kafkaTemplate;

    /**
     * Отправка сообщения с заказом в Kafka-топик "order-topic".
     * Ключ сообщения — id заказа в строковом виде.
     * В случае успеха или ошибки производится логирование.
     *
     * @param order ДТО-заказ, который необходимо отправить
     */

    // Если сообщение в кафку не отправилось, нужно сообщить пользователю о проблемах на сервисе
    @Override
    public void sendOrderToKafka(OrderKafkaDTO order) {
        try {
            kafkaTemplate
                    .send("order-topic", order.getId().toString(), order)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.error("Сообщение не отправлено в кафку. Произошла ошибка", exception);
                            // Оборачиваем и выбрасываем, чтобы пользователь получил ошибку
                            throw new KafkaDeliveryException("Ошибка на сервере");
                        } else {
                            log.info("Сообщение в кафку успешно отправлено: {}", result.getRecordMetadata());
                        }
                    }).get(); // Ожидаем завершения, чтобы можно было обработать ошибку синхронно
        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при отправке сообщения в кафку", e);
            throw new KafkaDeliveryException("Ошибка на сервере");
        }
    }

}
