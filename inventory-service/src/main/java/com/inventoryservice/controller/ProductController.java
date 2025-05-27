package com.inventoryservice.controller;

import com.inventoryservice.entity.ProductEntity;
import com.inventoryservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/all")
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
     * @param productEntities объект или объекты товара, полученного из тела запроса
     * @return созданный товар и HTTP-статус 201 Created с Location-заголовком
     */
    @Operation(summary = "Создать товар")
    @PostMapping("/create")
    public ResponseEntity<List<ProductEntity>> create(@RequestBody List<ProductEntity> productEntities) {
        List<ProductEntity> createdProductEntities = service.create(productEntities);

        return ResponseEntity.ok(createdProductEntities);

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
