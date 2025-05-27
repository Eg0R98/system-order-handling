package com.orderservice.controller;

import com.orderservice.entity.UserEntity;
import com.orderservice.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
/**
 * Контроллер для выполнения операций CRUD над пользователями.
 * Доступ к методам контроллера ограничен ролями с правами администратора.
 */
@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserCrudController {

    private final AdminUserService serviceAdmin;

    /**
     * Получение всех пользователей
     * @return HTTP 200 OK и список всех пользователей
     */
    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAll() {
        List<UserEntity> userEntities = serviceAdmin.findAll();
        return ResponseEntity.ok(userEntities);
    }

    /**
     * Получение пользователя по id
     * @param id пользователя
     * @return HTTP 200 OK и найденный пользователь
     */
    @Operation(summary = "Получить пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getById(@PathVariable Long id) {
        UserEntity userEntity = serviceAdmin.findById(id);

        return ResponseEntity.ok(userEntity);
    }

    /**
     * Создание нового пользователя.
     *
     * @param userEntity данные нового пользователя
     * @return HTTP 201 Created и созданный объект пользователя
     */
    @Operation(summary = "Создать пользователя")
    @PostMapping("/create")
    public ResponseEntity<UserEntity> create(@RequestBody UserEntity userEntity) {
        UserEntity createdUserEntity = serviceAdmin.create(userEntity);

        URI location = URI.create(String.format("/userEntity/%d", createdUserEntity.getId()));

        return ResponseEntity.created(location).body(createdUserEntity);

    }

    /**
     * Обновление существующего пользователя по id.
     *
     * @param id пользователя
     * @param userEntity данные пользователя для обновления
     * @return HTTP 200 OK и обновлённый пользователь
     */
    @Operation(summary = "Обновить пользователя по id")
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> update(@PathVariable Long id, @RequestBody UserEntity userEntity) {
        UserEntity updatedUserEntity = serviceAdmin.update(userEntity, id);

        return ResponseEntity.ok(updatedUserEntity);

    }

    /**
     * Удаление пользователя по id.
     *
     * @param id пользователя
     * @return HTTP 204 No Content
     */
    @Operation(summary = "Удалить пользователя по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        serviceAdmin.delete(id);

        return ResponseEntity.noContent().build();
    }


}
