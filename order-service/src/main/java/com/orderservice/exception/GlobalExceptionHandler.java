package com.orderservice.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений
 * Предназначен для кастомной обработки исключений
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Обработка EntityNotFoundException
     * @param e - объект класса EntityNotFoundException
     * @return кастомный ответ, содержащий статус 404 и сообщение об отсутствии нужной сущности
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        log.warn("Entity not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Обработка NotRequiredProductException
     * @param e - объект класса NotRequiredProductException
     * @return кастомный ответ, содержащий статус 400 и сообщение об отсутствии нужных товаров
     */
    @ExceptionHandler(NotRequiredProductException.class)
    public ResponseEntity<String> handleNotRequiredProduct(NotRequiredProductException e) {
        log.warn("Missing products", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.valueOf("text/plain; charset=UTF-8"))
                .body(e.getMessage());
    }

}