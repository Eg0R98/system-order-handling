package com.orderservice.service.impl;

import com.orderservice.dto.OrderDTOFoKafka;
import com.orderservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Реализация сервиса {@link KafkaProducerService} для отправки сообщений в Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, OrderDTOFoKafka> kafkaTemplate;

    /**
     * Отправка сообщения с заказом в Kafka-топик "order-topic".
     * Ключ сообщения — id заказа в строковом виде.
     * В случае успеха или ошибки производится логирование.
     *
     * @param order ДТО-заказ, который необходимо отправить
     */
    @Override
    public void sendOrderToKafka(OrderDTOFoKafka order){

        CompletableFuture<SendResult<String, OrderDTOFoKafka>> future = kafkaTemplate.
                send("order-topic", order.getId().toString(), order);

        future.whenComplete((result, exception) -> {
            if(exception != null){
                log.error("Сообщение не отправлено в кафку. Произошла ошибка");
            }
            else {
                log.info("Сообщение в кафку успешно отправлено: {}", result.getRecordMetadata());
            }
        });

    }

}
