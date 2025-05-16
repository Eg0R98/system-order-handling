package com.notificationservice.kafka;

import com.notificationservice.dto.OrderDTOFoKafka;
import com.notificationservice.entity.Order;
import com.notificationservice.exception.NonRetryableException;
import com.notificationservice.exception.RetryableException;
import com.notificationservice.mapping.OrderMapper;
import com.notificationservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderKafkaListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderMapper mapper;

    private final OrderRepository repository;

    @KafkaListener(topics = "order-topic", groupId = "order-events",
            containerFactory = "kafkaListenerContainerFactory")
    public void processOrder(OrderDTOFoKafka orderDTO) {

        logger.info("Получен orderDTO: {}", orderDTO);

        UUID orderId = orderDTO.getId();

        try {
            if (repository.existsById(orderId)) {
                logger.info("Order {} уже обработан.", orderId);
                return;
            }

            Order orderEntity = mapper.mapToOrderWithProducts(orderDTO);
            logger.info("Order {} сохранен в бд.", orderId);
            repository.save(orderEntity);

        } catch (ResourceAccessException e) {
            logger.error(e.getMessage());
            throw new RetryableException(e);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NonRetryableException(e);
        }
    }
}


