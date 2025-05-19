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
/**
 * CRUD-контроллер для товаров
 * Предоставляет REST API для операций создания, получения и удаления товаров.
*/
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    /**
     * Получение всех товаров.
     *
     * @return список всех товаров в виде ResponseEntity с HTTP-статусом 200 OK
     */
    @Operation(summary = "Получить все товары")
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = service.findAll();

        return ResponseEntity.ok(products);
    }

    /**
     * Получение товара по его идентификатору.
     *
     * @param id UUID товара
     * @return товар с переданным id и HTTP-статусом 200 OK
     */
    @Operation(summary = "Получить товар по id")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable UUID id) {
        Product product = service.findById(id);

        return ResponseEntity.ok(product);
    }

    /**
     * Создание нового товара.
     *
     * @param product объект товара, полученный из тела запроса
     * @return созданный товар и HTTP-статус 201 Created с Location-заголовком
     */
    @Operation(summary = "Создать товар")
    @PostMapping("/create")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product createdProduct = service.create(product);

        URI location = URI.create(String.format("/product/create/%s", createdProduct.getId()));

        return ResponseEntity.created(location).body(createdProduct);

    }

    /**
     * Удаление товара по его идентификатору.
     *
     * @param id UUID товара
     * @return HTTP-статус 204 No Content при успешном удалении
     */
    @Operation(summary = "Удалить товар по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
