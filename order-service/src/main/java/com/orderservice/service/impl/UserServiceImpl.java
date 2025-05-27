package com.orderservice.service.impl;

import com.orderservice.entity.UserEntity;
import com.orderservice.exception.NotUserNameException;
import com.orderservice.repository.UserRepository;
import com.orderservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link UserService}, обеспечивающая взаимодействие с репозиторием пользователей
 * и контекстом безопасности Spring Security.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

   private final UserRepository repository;


    /**
     * Создание пользователя с предварительной проверкой уникальности имени и email.
     *
     * @param userEntity объект пользователя
     * @return сохранённый пользователь
     * @throws RuntimeException если имя пользователя или email уже заняты
     */
    public UserEntity create(UserEntity userEntity) {
        if (repository.existsByUsername(userEntity.getUsername())) throw new NotUserNameException("Пользователь с таким именем уже существует");
        return repository.save(userEntity);
    }

    /**
     * Получение пользователя по имени
     * @param username имя пользователя
     * @return пользователь
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public UserEntity getByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Предоставляет реализацию {@link UserDetailsService}, которая используется Spring Security.
     *
     * @return реализация UserDetailsService
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего аутентифицированного пользователя из контекста безопасности.
     *
     * @return текущий пользователь
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public UserEntity getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    /**
     * Получение id текущего пользователя из контекста безопасности.
     *
     * @return id текущего пользователя
     * @throws IllegalStateException если объект principal не является экземпляром UserEntity
     */
    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserEntity userEntity) {
            return userEntity.getId();
        }
        throw new IllegalStateException("Пользователь не найден в контексте безопасности");
    }


}
