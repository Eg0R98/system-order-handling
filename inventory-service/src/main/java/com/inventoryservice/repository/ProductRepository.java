package com.inventoryservice.repository;

import com.inventoryservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Product.
 * Расширяет JpaRepository для предоставления стандартных CRUD-операций.
 * Использует UUID в качестве типа идентификатора.
 */
public interface ProductRepository extends JpaRepository<Product, UUID> {

}
