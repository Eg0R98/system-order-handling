package com.inventoryservice.controller;

import com.inventoryservice.entity.ProductEntity;
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
    public ResponseEntity<List<ProductEntity>> getAll() {
        List<ProductEntity> productEntities = service.findAll();

        return ResponseEntity.ok(productEntities);
    }

    /**
     * Получение товара по его идентификатору.
     *
     * @param id UUID товара
     * @return товар с переданным id и HTTP-статусом 200 OK
     */
    @Operation(summary = "Получить товар по id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getById(@PathVariable UUID id) {
        ProductEntity productEntity = service.findById(id);

        return ResponseEntity.ok(productEntity);
    }

    /**
     * Создание нового товара.
     *
     * @param productEntity объект товара, полученный из тела запроса
     * @return созданный товар и HTTP-статус 201 Created с Location-заголовком
     */
    @Operation(summary = "Создать товар")
    @PostMapping("/create")
    public ResponseEntity<ProductEntity> create(@RequestBody ProductEntity productEntity) {
        ProductEntity createdProductEntity = service.create(productEntity);

        URI location = URI.create(String.format("/productEntity/create/%s", createdProductEntity.getId()));

        return ResponseEntity.created(location).body(createdProductEntity);

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
