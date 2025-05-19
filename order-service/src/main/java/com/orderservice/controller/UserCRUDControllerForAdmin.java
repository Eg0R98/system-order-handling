package com.orderservice.controller;

import com.orderservice.entity.User;
import com.orderservice.service.UserCRUDServiceForAdmin;
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
public class UserCRUDControllerForAdmin {

    private final UserCRUDServiceForAdmin serviceForAdmin;

    /**
     * Получение всех пользователей
     * @return HTTP 200 OK и список всех пользователей
     */
    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = serviceForAdmin.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Получение пользователя по id
     * @param id пользователя
     * @return HTTP 200 OK и найденный пользователь
     */
    @Operation(summary = "Получить пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = serviceForAdmin.findById(id);

        return ResponseEntity.ok(user);
    }

    /**
     * Создание нового пользователя.
     *
     * @param user данные нового пользователя
     * @return HTTP 201 Created и созданный объект пользователя
     */
    @Operation(summary = "Создать пользователя")
    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user) {
        User createdUser = serviceForAdmin.create(user);

        URI location = URI.create(String.format("/user/%d", createdUser.getId()));

        return ResponseEntity.created(location).body(createdUser);

    }

    /**
     * Обновление существующего пользователя по id.
     *
     * @param id пользователя
     * @param user данные пользователя для обновления
     * @return HTTP 200 OK и обновлённый пользователь
     */
    @Operation(summary = "Обновить пользователя по id")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = serviceForAdmin.update(user, id);

        return ResponseEntity.ok(updatedUser);

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

        serviceForAdmin.delete(id);

        return ResponseEntity.noContent().build();
    }


}
