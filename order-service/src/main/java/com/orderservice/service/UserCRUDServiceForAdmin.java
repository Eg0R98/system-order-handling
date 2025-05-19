package com.orderservice.service;

import com.orderservice.entity.User;

import java.util.List;

/**
 * Сервис для администрирования пользователей.
 * Предоставляет базовые CRUD-операции: создание, чтение, обновление и удаление пользователей.
 */
public interface UserCRUDServiceForAdmin {

    /**
     * Получение список всех пользователей.
     *
     * @return список всех пользователей в системе
     */
    List<User> findAll();

    /**
     * Поиск пользователя по id.
     *
     * @param id пользователя
     * @return найденный пользователь
     */
    User findById(Long id);

    /**
     * Создание нового пользователя.
     *
     * @param user объект пользователя
     * @return созданный пользователь с присвоенным id
     */
    User create(User user);

    /**
     * Обновление существующего пользователя.
     *
     * @param user обновлённые данные пользователя
     * @param id пользователя, которого нужно обновить
     * @return обновлённый пользователь
     */
    User update(User user, Long id);

    /**
     * Удаление пользователя по его id.
     *
     * @param id пользователя
     */
    void delete(Long id);
}
