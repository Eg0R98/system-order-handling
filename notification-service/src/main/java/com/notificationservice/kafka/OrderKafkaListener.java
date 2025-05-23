package com.notificationservice.kafka;

import com.notificationservice.dto.OrderKafkaDTO;
import com.notificationservice.entity.OrderEntity;
import com.notificationservice.exception.NonRetryableException;
import com.notificationservice.exception.RetryableException;
import com.notificationservice.mapping.OrderMapper;
import com.notificationservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.util.UUID;

/**
 * Компонент для прослушивания Kafka-топика с событиями заказов.
 * При получении сообщения конвертирует DTO заказа в сущность и сохраняет её в базу данных.
 * Обрабатывает исключения, разделяя ошибки, подлежащие повторным попыткам, и нет.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "order-topic", groupId = "order-events", containerFactory = "kafkaListenerContainerFactory")
public class OrderKafkaListener {

    private final OrderMapper mapper;

    private final OrderRepository repository;

    /**
     * Метод обработки сообщений из Kafka топика "order-topic".
     * Проверяет, был ли заказ уже сохранён, если нет — мапит и сохраняет новый заказ.
     * Логирует процесс и ошибки, выбрасывает исключения для управления повторными попытками.
     *
     * @param orderDTO DTO заказа, полученный из Kafka
     */
    @KafkaHandler
    public void processOrder(OrderKafkaDTO orderDTO) {

        log.info("Получен orderDTO: {}", orderDTO);

        UUID orderId = orderDTO.getId();

        try {
            if (repository.existsById(orderId)) {
                log.info("OrderEntity {} уже обработан.", orderId);
                return;
            }

            OrderEntity orderEntity = mapper.mapToOrderWithProducts(orderDTO);
            repository.save(orderEntity);
            log.info("OrderEntity {} сохранен в бд.", orderId);


        } catch (ResourceAccessException e) {
            log.error(e.getMessage());
            throw new RetryableException(e);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NonRetryableException(e);
        }
    }
}


