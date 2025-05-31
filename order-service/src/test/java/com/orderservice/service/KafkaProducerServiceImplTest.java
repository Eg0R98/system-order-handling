package com.orderservice.service;

import com.orderservice.dto.OrderKafkaDTO;
import com.orderservice.exception.KafkaDeliveryException;
import com.orderservice.service.impl.KafkaProducerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Unit-тесты для {@link KafkaProducerServiceImpl}.
 * Тестируется корректная отправка сообщений в Kafka-топик "order-topic" и обработка исключений,
 * возникающих при ошибках доставки сообщений.
 */
@ExtendWith(MockitoExtension.class)
public class KafkaProducerServiceImplTest {

    @Mock
    private KafkaTemplate<String, OrderKafkaDTO> kafkaTemplate;

    @InjectMocks
    private KafkaProducerServiceImpl kafkaProducerService;

    /**
     * Проверяет успешную отправку сообщения заказа в Kafka.
     * Убеждается, что {@link KafkaTemplate#send(String, Object, Object)} вызывается с правильными параметрами
     * и исключений не возникает.
     *
     * @throws Exception при ошибках в процессе тестирования
     */
    @Test
    void sendOrderToKafka_shouldSendSuccessfully() throws Exception {

        UUID uuid = UUID.randomUUID();
        OrderKafkaDTO order = new OrderKafkaDTO();
        order.setId(uuid);

        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, OrderKafkaDTO>> future = CompletableFuture.completedFuture(mock(SendResult.class));

        when(kafkaTemplate.send(anyString(), anyString(), any(OrderKafkaDTO.class))).thenReturn(future);

        assertDoesNotThrow(() -> kafkaProducerService.sendOrderToKafka(order));

        verify(kafkaTemplate, times(1)).send("order-topic", uuid.toString(), order);
    }

    /**
     * Проверяет, что при возникновении ошибки во время отправки сообщения в Kafka
     * выбрасывается {@link KafkaDeliveryException} с ожидаемым сообщением.
     *
     * @throws Exception при ошибках в процессе тестирования
     */
    @Test
    void sendOrderToKafka_shouldThrowKafkaDeliveryException_whenExecutionFails() throws Exception {
        UUID uuid = UUID.randomUUID();
        OrderKafkaDTO order = new OrderKafkaDTO();
        order.setId(uuid);

        CompletableFuture<SendResult<String, OrderKafkaDTO>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka error"));

        when(kafkaTemplate.send(anyString(), anyString(), any(OrderKafkaDTO.class))).thenReturn(future);

        KafkaDeliveryException exception = assertThrows(KafkaDeliveryException.class, () -> kafkaProducerService.sendOrderToKafka(order));

        assertEquals("Ошибка на сервере", exception.getMessage());
        verify(kafkaTemplate, times(1)).send("order-topic", uuid.toString(), order);
    }
}
