package com.orderservice.service.impl;

import com.orderservice.entity.UserEntity;
import com.orderservice.repository.UserRepository;
import com.orderservice.service.UserCRUDServiceAdmin;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация сервиса {@link UserCRUDServiceAdmin} для управления пользователями администратором.
 * Использует {@link UserRepository} для взаимодействия с базой данных.
 */
@Service
@RequiredArgsConstructor
public class UserCRUDServiceAdminImpl implements UserCRUDServiceAdmin {

    private final UserRepository repository;


    /**
     * Получение списка всех пользователей из базы данных.
     *
     * @return список всех пользователей
     */
    @Override
    public List<UserEntity> findAll() {
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
    public UserEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("UserEntity with id=%d not found", id)));
    }

    /**
     * Создание нового пользователя и сохранение его в базе данных.
     *
     * @param userEntity объект пользователя
     * @return сохранённый пользователь с присвоенным id
     */
    @Override
    public UserEntity create(UserEntity userEntity) {
        return repository.save(userEntity);
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param userEntity объект с обновлёнными данными
     * @param id пользователя для обновления
     * @return обновлённый пользователь
     * @throws EntityNotFoundException если пользователь не найден
     */
    @Override
    public UserEntity update(UserEntity userEntity, Long id) {

        UserEntity userEntityFromBD = repository.findById(id).orElseThrow(() -> new  EntityNotFoundException(String.format("UserEntity with id=%d not found", id)));

        userEntityFromBD.setUsername(userEntity.getUsername());
        userEntityFromBD.setPassword(userEntity.getPassword());
        userEntityFromBD.setRole(userEntity.getRole());

        return repository.save(userEntityFromBD);
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
