package com.notificationservice.service.impl;

import com.notificationservice.entity.Order;
import com.notificationservice.repository.OrderRepository;
import com.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Order findByOrderId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Order with id=%s not found", id)));
    }

    @Override
    public List<Order> findByUserId(Long id) {
        return repository.findByUserId(id).orElseThrow(() -> new RuntimeException(String.format("User with id=%d not found", id)));
    }

}
