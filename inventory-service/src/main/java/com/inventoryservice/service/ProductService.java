package com.inventoryservice.service;

import com.inventoryservice.entity.ProductEntity;

import java.util.List;
import java.util.UUID;
/**
 * Интерфейс сервиса для работы с товарами.
 * Определяет основные операции CRUD для сущности ProductEntity.
 */
public interface ProductService {

    /**
     * Получение списка всех товаров.
     * @return список всех продуктов
     */
    List<ProductEntity> findAll();

    /**
     * Поиск товара по его id.
     * @param id товара
     * @return найденный товар
     */
    ProductEntity findById(UUID id);

    /**
     * Создание нового товара.
     * @param productEntity объект товара для создания
     * @return созданный товар с заполненным идентификатором
     */
    ProductEntity create(ProductEntity productEntity);

    /**
     * Удаление товара по его id.
     * @param id товара
     */
    void delete(UUID id);
}
