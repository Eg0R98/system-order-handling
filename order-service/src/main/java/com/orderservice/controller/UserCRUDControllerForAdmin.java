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

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserCRUDControllerForAdmin {

    private final UserCRUDServiceForAdmin serviceForAdmin;

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = serviceForAdmin.findAll();

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Получить пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = serviceForAdmin.findById(id);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Создать пользователя")
    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user) {
        User createdUser = serviceForAdmin.create(user);

        URI location = URI.create(String.format("/user/create/%d", createdUser.getId()));

        return ResponseEntity.created(location).body(createdUser);

    }

    @Operation(summary = "Обновить пользователя по id")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = serviceForAdmin.update(user, id);

        return ResponseEntity.ok(updatedUser);

    }

    @Operation(summary = "Удалить пользователя по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        serviceForAdmin.delete(id);

        return ResponseEntity.noContent().build();
    }


}
