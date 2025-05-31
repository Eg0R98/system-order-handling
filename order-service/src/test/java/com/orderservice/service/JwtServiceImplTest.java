package com.orderservice.service;

import com.orderservice.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

    /**
     * Тесты для {@link JwtServiceImpl}, проверяющие генерацию, валидацию и извлечение данных из JWT токенов.
     * В тестах устанавливается секретный ключ через {@link ReflectionTestUtils}, чтобы имитировать настройку
     * из application properties.
     */
    @ExtendWith(MockitoExtension.class)
    public class JwtServiceImplTest {

        private JwtServiceImpl jwtService;

        private final String secretKey = Base64.getEncoder().encodeToString("1RbJhYZ2CnZsv2v/cJRVlX4OYis6N5RQ+CKPgiJAHl8=".getBytes());

        /**
         * Инициализация {@link JwtServiceImpl} и установка секретного ключа перед каждым тестом.
         */
        @BeforeEach
        void setUp() {

            jwtService = new JwtServiceImpl();
            ReflectionTestUtils.setField(jwtService, "jwtSigningKey", secretKey);
        }

        /**
         * Проверяет, что сгенерированный токен считается валидным для пользователя, для которого он был создан.
         */
        @Test
        void generateAndValidateToken_shouldReturnTrueForValidToken() {
            UserDetails userDetails = new User("testuser", "password", new ArrayList<>());

            String token = jwtService.generateToken(userDetails);

            boolean isValid = jwtService.isTokenValid(token, userDetails);

            assertTrue(isValid, "The generated token should be valid for the provided user");
        }

        /**
         * Проверяет, что метод {@code extractUserName} корректно извлекает имя пользователя из токена.
         */
        @Test
        void extractUserName_shouldReturnCorrectUsername() {
            UserDetails userDetails = new User("testuser", "password", new ArrayList<>());

            String token = jwtService.generateToken(userDetails);

            String extractedUsername = jwtService.extractUserName(token);

            assertEquals("testuser", extractedUsername, "The extracted username should match the original username");
        }

        /**
         * Проверяет, что токен становится невалидным, если валидировать его с другим именем пользователя.
         */
        @Test
        void isTokenValid_shouldReturnFalseForInvalidUsername() {
            UserDetails userDetails = new User("testuser", "password", new ArrayList<>());

            String token = jwtService.generateToken(userDetails);

            UserDetails otherUser = new User("otheruser", "password", new ArrayList<>());

            boolean isValid = jwtService.isTokenValid(token, otherUser);

            assertFalse(isValid, "The token should be invalid if checked against a different username");
        }


    }
