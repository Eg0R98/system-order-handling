package com.orderservice.controller;

import com.orderservice.dto.AuthRequest;
import com.orderservice.dto.RegRequest;
import com.orderservice.security.JwtAuthenticationResponse;
import com.orderservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/reg")
    public JwtAuthenticationResponse registration(@RequestBody @Valid RegRequest request) {
        return authenticationService.registration(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody @Valid AuthRequest request) {
        return authenticationService.authentication(request);
    }

    @Operation(summary = "Обновление токена")
    @GetMapping("/refresh")
    public JwtAuthenticationResponse refreshToken(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Отсутствует токен");
        }

        String token = authHeader.substring(7);
        return authenticationService.refreshToken(token);
    }
}