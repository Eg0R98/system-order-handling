package com.notificationservice.exception;

/**
 * Исключение, которое указывает на ошибку, не подлежащую повторной попытке обработки.
 * Используется для сигнализации, что операцию повторять не нужно.
 */
public class NonRetryableException extends RuntimeException {

    public NonRetryableException(String message) {
        super(message);
    }

    public NonRetryableException(Throwable cause) {
        super(cause);
    }
}
