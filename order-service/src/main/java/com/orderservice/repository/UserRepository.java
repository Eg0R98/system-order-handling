package com.orderservice.repository;

import com.orderservice.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий работает с сущностью User
 * Он расширяет стандартный JpaRepository
 * А также в нем объявлены кастомные методы
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Поиск пользователя по имени
     * @param username - имя пользователя
     * @return пользователя в обертке Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Поиск пользователя по имени
     * @param username - имя пользователя
     */
    boolean existsByUsername(String username);

    /**
     * Поиск пользователя по email
     * @param email - имя пользователя
     */
    boolean existsByEmail(String email);

    /**
     * Удаление пользователя по id
     * @param id пользователя
     */
    void deleteById(@NonNull Long id);

    /**
     * Поиск пользователя по id
     * @param id пользователя
     * @return пользователя в обертке Optional
     */
    @NonNull
    Optional<User> findById(@NonNull Long id);
}
