package com.orderservice.controller;

import com.orderservice.dto.AuthRequest;
import com.orderservice.dto.JwtAuthenticationResponse;
import com.orderservice.dto.RegRequest;
import com.orderservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для регистрации, авторизации пользователя и обновления токена.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;

    /**
     * Регистрация нового пользователя
     * @param request содержит нужные данные для регистрации
     * @return JwtAuthenticationResponse, хранящий токен в виде строки
     */
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/reg")
    public JwtAuthenticationResponse registration(@RequestBody @Valid RegRequest request) {
        return authenticationService.registration(request);
    }

    /**
     * Авторизация пользователя
     * @param request вмещает необходимые данные для авторизации
     * @return JwtAuthenticationResponse, хранящий токен в виде строки
     */
    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody @Valid AuthRequest request) {
        return authenticationService.authentication(request);
    }

    /**
     * Обновление токена
     * @param authHeader хранить данные из перехваченного заголовка Authorization
     * @return JwtAuthenticationResponse, хранящий токен в виде строки
     */
    @Operation(summary = "Обновление токена")
    @GetMapping("/token/refresh")
    public JwtAuthenticationResponse refreshToken(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Отсутствует токен");
        }

        String token = authHeader.substring(7);
        return authenticationService.refreshToken(token);
    }
}