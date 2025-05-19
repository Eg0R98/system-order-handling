package com.notificationservice.service;

import com.notificationservice.entity.Order;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный интерфейс для работы с заказами.
 * Определяет базовые операции получения заказов.
 */
public interface OrderService {

    /**
     * Получить список всех заказов.
     *
     * @return список заказов
     */
    List<Order> findAll();

    /**
     * Найти заказ по его идентификатору.
     *
     * @param id заказа
     * @return найденный заказ
     */
    Order findByOrderId(UUID id);

    /**
     * Найти заказы по идентификатору пользователя.
     *
     * @param id пользователя
     * @return список заказов пользователя
     */
    List<Order> findByUserId(Long id);
}
