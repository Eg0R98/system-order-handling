package com.orderservice.service.impl;

import com.orderservice.dto.OrderDTOFoKafka;
import com.orderservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, OrderDTOFoKafka> kafkaTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void sendOrderToKafka(OrderDTOFoKafka order){

        CompletableFuture<SendResult<String, OrderDTOFoKafka>> future = kafkaTemplate.
                send("order-topic", order.getId().toString(), order);

        future.whenComplete((result, exception) -> {
            if(exception != null){
                LOGGER.error("Сообщение не отправлено в кафку. Произошла ошибка");
            }
            else {
                LOGGER.info("Сообщение в кафку успешно отправлено: {}", result.getRecordMetadata());
            }
        });

    }

}
