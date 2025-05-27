package com.notificationservice.exception;

/**
 * Исключение, которое указывает на ошибку, подлежащую повторной попытке обработки.
 * Используется для сигнализации, что операцию следует попробовать выполнить повторно.
 */
public class RetryableException extends RuntimeException {

    public RetryableException(String message) {
        super(message);
    }

    public RetryableException(Throwable cause) {
        super(cause);
    }
}
