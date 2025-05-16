package com.inventoryservice.service.impl;

import com.inventoryservice.entity.Product;
import com.inventoryservice.repository.ProductRepository;
import com.inventoryservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Product with id=%s not found", id)));
    }

    @Override
    public Product create(Product product) {
        return repository.save(product);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
