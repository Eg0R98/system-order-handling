package com.orderservice.service;

import com.orderservice.dto.AuthRequest;
import com.orderservice.dto.JwtAuthenticationResponse;
import com.orderservice.dto.RegRequest;
import com.orderservice.entity.UserEntity;
import com.orderservice.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link AuthenticationServiceImpl}, проверяющие регистрацию, аутентификацию и обновление токена.
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    /**
     * Проверяет, что метод {@code registration} корректно регистрирует пользователя
     * и возвращает JWT токен.
     */
    @Test
    void registration_shouldReturnJwtResponse() {
        RegRequest request = new RegRequest("testuser", "password");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("fake-jwt-token");

        JwtAuthenticationResponse response = authenticationService.registration(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());

        verify(userService).create(any(UserEntity.class));
        verify(jwtService).generateToken(any(UserEntity.class));
    }

    /**
     * Проверяет, что метод {@code authentication} корректно аутентифицирует пользователя
     * и возвращает JWT токен.
     */
    @Test
    void authentication_shouldReturnJwtResponse() {
        AuthRequest request = new AuthRequest("testuser", "password");

        UserDetails userDetails = new User("testuser", "encodedPassword", Collections.emptyList());

        when(userService.userDetailsService()).thenReturn(username -> userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("fake-jwt-token");

        JwtAuthenticationResponse response = authenticationService.authentication(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userDetails);
    }


    /**
     * Проверяет, что метод {@code refreshToken} возвращает новый JWT, если переданный токен валиден.
     */
    @Test
    void refreshToken_shouldReturnNewJwtIfValid() {
        String oldToken = "old-token";
        String userName = "testuser";

        UserDetails userDetails = new User("testuser", "encodedPassword", Collections.emptyList());

        when(jwtService.extractUserName(oldToken)).thenReturn(userName);
        when(userService.userDetailsService()).thenReturn(name -> userDetails);
        when(jwtService.isTokenValid(oldToken, userDetails)).thenReturn(true);
        when(jwtService.generateToken(userDetails)).thenReturn("new-token");

        JwtAuthenticationResponse response = authenticationService.refreshToken(oldToken);

        assertNotNull(response);
        assertEquals("new-token", response.getToken());

        verify(jwtService).generateToken(userDetails);
    }

    /**
     * Проверяет, что метод {@code refreshToken} выбрасывает исключение,
     * если переданный токен недействителен.
     */
    @Test
    void refreshToken_shouldThrowExceptionIfTokenInvalid() {
        String oldToken = "old-token";
        String userName = "testuser";

        UserDetails userDetails = new User("testuser", "encodedPassword", Collections.emptyList());

        when(jwtService.extractUserName(oldToken)).thenReturn(userName);
        when(userService.userDetailsService()).thenReturn(name -> userDetails);
        when(jwtService.isTokenValid(oldToken, userDetails)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authenticationService.refreshToken(oldToken));

        assertEquals("Недопустимый токен", exception.getMessage());
    }

}
