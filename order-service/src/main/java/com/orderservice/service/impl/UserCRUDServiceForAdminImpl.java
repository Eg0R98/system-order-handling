package com.orderservice.service.impl;

import com.orderservice.entity.User;
import com.orderservice.repository.UserRepository;
import com.orderservice.service.UserCRUDServiceForAdmin;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация сервиса {@link UserCRUDServiceForAdmin} для управления пользователями администратором.
 * Использует {@link UserRepository} для взаимодействия с базой данных.
 */
@Service
@RequiredArgsConstructor
public class UserCRUDServiceForAdminImpl implements UserCRUDServiceForAdmin {

    private final UserRepository repository;


    /**
     * Получение списка всех пользователей из базы данных.
     *
     * @return список всех пользователей
     */
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * Поиск пользователя по id.
     *
     * @param id пользователя
     * @return найденный пользователь
     * @throws EntityNotFoundException если пользователь не найден
     */
    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%d not found", id)));
    }

    /**
     * Создание нового пользователя и сохранение его в базе данных.
     *
     * @param user объект пользователя
     * @return сохранённый пользователь с присвоенным id
     */
    @Override
    public User create(User user) {
        return repository.save(user);
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param user объект с обновлёнными данными
     * @param id пользователя для обновления
     * @return обновлённый пользователь
     * @throws EntityNotFoundException если пользователь не найден
     */
    @Override
    public User update(User user, Long id) {

        User userFromBD = repository.findById(id).orElseThrow(() -> new  EntityNotFoundException(String.format("User with id=%d not found", id)));

        userFromBD.setUsername(user.getUsername());
        userFromBD.setEmail(user.getEmail());
        userFromBD.setPassword(user.getPassword());
        userFromBD.setRole(user.getRole());

        return repository.save(userFromBD);
    }

    /**
     * Удаляет пользователя по id.
     *
     * @param id пользователя
     */
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }


}
