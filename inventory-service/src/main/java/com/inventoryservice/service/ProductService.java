package com.inventoryservice.service;

import com.inventoryservice.entity.Product;

import java.util.List;
import java.util.UUID;
/**
 * Интерфейс сервиса для работы с товарами.
 * Определяет основные операции CRUD для сущности Product.
 */
public interface ProductService {

    /**
     * Получение списка всех товаров.
     * @return список всех продуктов
     */
    List<Product> findAll();

    /**
     * Поиск товара по его id.
     * @param id товара
     * @return найденный товар
     */
    Product findById(UUID id);

    /**
     * Создание нового товара.
     * @param product объект товара для создания
     * @return созданный товар с заполненным идентификатором
     */
    Product create(Product product);

    /**
     * Удаление товара по его id.
     * @param id товара
     */
    void delete(UUID id);
}
