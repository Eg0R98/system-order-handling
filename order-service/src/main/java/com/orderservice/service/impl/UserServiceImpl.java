package com.orderservice.service.impl;

import com.orderservice.entity.User;
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
     * @param user объект пользователя
     * @return сохранённый пользователь
     * @throws RuntimeException если имя пользователя или email уже заняты
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {

            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return repository.save(user);
    }

    /**
     * Получение пользователя по имени
     * @param username имя пользователя
     * @return пользователь
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public User getByUsername(String username) {
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
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    /**
     * Получение id текущего пользователя из контекста безопасности.
     *
     * @return id текущего пользователя
     * @throws IllegalStateException если объект principal не является экземпляром User
     */
    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user.getId();
        }
        throw new IllegalStateException("Пользователь не найден в контексте безопасности");
    }


}
