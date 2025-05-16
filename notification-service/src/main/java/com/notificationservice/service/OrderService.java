package com.notificationservice.service;

import com.notificationservice.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<Order> findAll();

    Order findByOrderId(UUID id);

    List<Order> findByUserId(Long id);
}
