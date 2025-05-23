package com.orderservice.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

//    @ExceptionHandler(NotRequiredProductException.class)
//    public ResponseEntity<?> handleNotRequiredProductException(NotRequiredProductException ex) {
//        // Извлекаем имена товаров прямо здесь
//        List<String> missingProductNames = ex.getMissingProducts().stream()
//                .map(UnsuccessfulProductDTOFromInventoryService::getName) // или другой метод
//                .collect(Collectors.toList());
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("message", ex.getMessage());
//        body.put("missingProducts", missingProductNames);
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
//    }
}