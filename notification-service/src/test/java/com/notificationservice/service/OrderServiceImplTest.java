package com.notificationservice.service;

import com.notificationservice.entity.OrderEntity;
import com.notificationservice.repository.OrderRepository;
import com.notificationservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для сервиса {@link OrderServiceImpl}, отвечающего за управление заказами.
 * Используется Mockito для создания моков и проверки взаимодействий.
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderServiceImpl service;

    /**
     * Проверяет, что метод {@code findAll} возвращает список всех заказов.
     */
    @Test
    void findAll_returnsListOfOrders(){
        List<OrderEntity> orderEntities = List.of(
                OrderEntity.builder().id(UUID.randomUUID()).userId(3L).build(),
                OrderEntity.builder().id(UUID.randomUUID()).userId(4L).build());

        when(repository.findAll()).thenReturn(orderEntities);

        List<OrderEntity> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(OrderEntity::getUserId)
                .containsExactly(3L, 4L);

        verify(repository).findAll();
    }

    /**
     * Проверяет, что метод {@code findByOrderId} возвращает заказ по id, если он существует.
     */
    @Test
    void findByOrderId_whenOrderExists_returnsOrder() {
        UUID id = UUID.randomUUID();
        OrderEntity orderEntity = OrderEntity.builder().id(id).userId(4L).build();

        when(repository.findById(id)).thenReturn(Optional.of(orderEntity));

        OrderEntity result = service.findByOrderId(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);

        verify(repository).findById(id);
    }

    /**
     * Проверяет, что метод {@code findById} выбрасывает {@link RuntimeException},
     * если заказ с указанным id не найден.
     */
    @Test
    void findByOrderId_whenOrderNotFound_throwsException() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByOrderId(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("OrderEntity with id=%s not found", id);

        verify(repository).findById(id);
    }

    /**
     * Проверяет, что метод {@code findByUserId} возвращает заказ или заказы по id пользователя, если он существует.
     */
    @Test
    void findByUserId_whenOrderExists_returnsOrder() {
        Long userId = 4L;
        List<OrderEntity> orderEntities = List.of(
                OrderEntity.builder().id(UUID.randomUUID()).userId(userId).build(),
                OrderEntity.builder().id(UUID.randomUUID()).userId(userId).build());

        when(repository.findByUserId(orderEntities.getLast().getUserId())).thenReturn(Optional.of(orderEntities));

        List<OrderEntity> result = service.findByUserId(userId);

        assertThat(result).isNotNull();
        assertThat(result.getLast().getUserId()).isEqualTo(userId);

        verify(repository).findByUserId(userId);
    }

    /**
     * Проверяет, что метод {@code findByUserId} выбрасывает {@link RuntimeException},
     * если заказ с указанным id пользователя не найден.
     */
    @Test
    void findByUserId_whenOrderNotFound_throwsException() {
        Long userId = 4L;

        when(repository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByUserId(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User with id=%d not found", userId);

        verify(repository).findByUserId(userId);
    }




}
