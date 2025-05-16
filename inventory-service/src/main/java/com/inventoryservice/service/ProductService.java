package com.inventoryservice.service;

import com.inventoryservice.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll();

    Product findById(UUID id);

    Product create(Product product);

    void delete(UUID id);
}
