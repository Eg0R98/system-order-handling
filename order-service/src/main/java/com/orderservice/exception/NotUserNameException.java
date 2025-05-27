package com.orderservice.exception;

/**
 * Исключение выбрасывается, если не найден пользователь с нужным именем
 */
public class NotUserNameException extends RuntimeException {
    public NotUserNameException(String message) {
        super(message);
    }
}
