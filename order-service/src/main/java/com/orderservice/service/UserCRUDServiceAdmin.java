package com.orderservice.service;

import com.orderservice.entity.UserEntity;

import java.util.List;

/**
 * Сервис для администрирования пользователей.
 * Предоставляет базовые CRUD-операции: создание, чтение, обновление и удаление пользователей.
 */
public interface UserCRUDServiceAdmin {

    /**
     * Получение список всех пользователей.
     *
     * @return список всех пользователей в системе
     */
    List<UserEntity> findAll();

    /**
     * Поиск пользователя по id.
     *
     * @param id пользователя
     * @return найденный пользователь
     */
    UserEntity findById(Long id);

    /**
     * Создание нового пользователя.
     *
     * @param userEntity объект пользователя
     * @return созданный пользователь с присвоенным id
     */
    UserEntity create(UserEntity userEntity);

    /**
     * Обновление существующего пользователя.
     *
     * @param userEntity обновлённые данные пользователя
     * @param id пользователя, которого нужно обновить
     * @return обновлённый пользователь
     */
    UserEntity update(UserEntity userEntity, Long id);

    /**
     * Удаление пользователя по его id.
     *
     * @param id пользователя
     */
    void delete(Long id);
}
