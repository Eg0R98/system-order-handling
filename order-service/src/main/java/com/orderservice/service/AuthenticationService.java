package com.orderservice.service;

import com.orderservice.dto.AuthRequest;
import com.orderservice.dto.JwtAuthenticationResponse;
import com.orderservice.dto.RegRequest;

/**
 * Сервис для работы с аутентификацией и регистрацией пользователей.
 */
public interface AuthenticationService {

    /**
     * Регистрация нового пользователя.
     *
     * @param request данные для регистрации
     * @return JWT-ответ с токеном
     */
    JwtAuthenticationResponse registration(RegRequest request);

    /**
     * Аутентификация пользователя.
     *
     * @param request данные для входа
     * @return JWT-ответ с токеном
     */
    JwtAuthenticationResponse authentication(AuthRequest request);

    /**
     * Обновление JWT-токена.
     *
     * @param token текущий токен
     * @return новый JWT-токен
     */
    JwtAuthenticationResponse refreshToken(String token);
}
