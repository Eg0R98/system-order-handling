package com.orderservice.service;

import com.orderservice.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Сервисный интерфейс для работы с пользователями.
 * Содержит методы для управления пользователями и получения информации о текущем пользователе.
 */
@Component
public interface UserService {

     /**
      * Создание нового пользователя.
      *
      * @param user объект пользователя
      * @return созданный пользователь
      */
     User create(User user);

     /**
      * Получение пользователя по его имени.
      *
      * @param username имя пользователя
      * @return пользователь
      */
     User getByUsername(String username);

     /**
      * Возвращение реализацию {@link UserDetailsService}, основанную на методе получения пользователя по имени.
      *
      * @return реализация UserDetailsService
      */
     UserDetailsService userDetailsService();

     /**
      * Получение текущего аутентифицированного пользователя из контекста безопасности.
      *
      * @return текущий пользователь
      */
     User getCurrentUser();

     /**
      * Получение id текущего пользователя.
      *
      * @return id текущего пользователя
      */
     Long getCurrentUserId();

}
