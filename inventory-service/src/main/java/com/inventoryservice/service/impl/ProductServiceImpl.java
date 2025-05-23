package com.inventoryservice.service.impl;

import com.inventoryservice.entity.ProductEntity;
import com.inventoryservice.repository.ProductRepository;
import com.inventoryservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса ProductService для управления товарами.
 * Содержит операции для получения, создания и удаления товаров в базе данных.
 * Использует ProductRepository для взаимодействия с уровнем хранения данных.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    /**
     * Получение всех товаров из базы данных.
     *
     * @return список всех объектов ProductEntity
     */
    @Override
    public List<ProductEntity> findAll() {
        return repository.findAll();
    }

    /**
     * Получение товара по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор товара
     * @return найденный объект ProductEntity
     * @throws RuntimeException если товар не найден
     */
    @Override
    public ProductEntity findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("ProductEntity with id=%s not found", id)));
    }

    /**
     * Создание нового товара.
     *
     * @param productEntity объект ProductEntity для сохранения
     * @return сохранённый объект ProductEntity
     */
    @Override
    public ProductEntity create(ProductEntity productEntity) {
        return repository.save(productEntity);
    }

    /**
     * Удаление товара по его id.
     *
     * @param id товара, подлежащего удалению
     */
    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
