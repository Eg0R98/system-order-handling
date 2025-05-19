package com.notificationservice.service.impl;

import com.notificationservice.entity.Order;
import com.notificationservice.repository.OrderRepository;
import com.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервисного интерфейса {@link OrderService}.
 * Осуществляет взаимодействие с репозиторием заказов.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    /**
     * Получить список всех заказов.
     *
     * @return список заказов
     */
    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    /**
     * Найти заказ по его идентификатору.
     *
     * @param id заказа
     * @return найденный заказ
     * @throws RuntimeException если заказ с данным id не найден
     */
    @Override
    public Order findByOrderId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Order with id=%s not found", id)));
    }

    /**
     * Найти заказы по идентификатору пользователя.
     *
     * @param id пользователя
     * @return список заказов пользователя
     * @throws RuntimeException если пользователь с данным id не найден
     */
    @Override
    public List<Order> findByUserId(Long id) {
        return repository.findByUserId(id).orElseThrow(() -> new RuntimeException(String.format("User with id=%d not found", id)));
    }

}
