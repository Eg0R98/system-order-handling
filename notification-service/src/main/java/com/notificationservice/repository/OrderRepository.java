package com.notificationservice.repository;

import com.notificationservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Order.
 * Предоставляет базовые CRUD операции благодаря JpaRepository.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {


  /**
   * Поиск списка заказов по идентификатору пользователя.
   *
   * @param id идентификатор пользователя
   * @return Optional со списком заказов пользователя, либо пустой, если заказы не найдены
   */
  Optional<List<Order>> findByUserId(Long id);
}
