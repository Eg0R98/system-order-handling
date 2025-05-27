package com.notificationservice.repository;

import com.notificationservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью OrderEntity.
 * Предоставляет базовые CRUD операции благодаря JpaRepository.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {


  /**
   * Поиск списка заказов по идентификатору пользователя.
   *
   * @param id идентификатор пользователя
   * @return Optional со списком заказов пользователя, либо пустой, если заказы не найдены
   */
  Optional<List<OrderEntity>> findByUserId(Long id);
}
