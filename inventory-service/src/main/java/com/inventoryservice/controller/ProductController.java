package com.inventoryservice.controller;

import com.inventoryservice.entity.Product;
import com.inventoryservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Operation(summary = "Получить все товары")
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = service.findAll();

        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Получить товар по id")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable UUID id) {
        Product product = service.findById(id);

        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Создать товар")
    @PostMapping("/create")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product createdProduct = service.create(product);

        URI location = URI.create(String.format("/product/create/%s", createdProduct.getId()));

        return ResponseEntity.created(location).body(createdProduct);

    }

    @Operation(summary = "Удалить товар по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
