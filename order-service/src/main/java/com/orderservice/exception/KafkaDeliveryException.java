package com.orderservice.exception;

/**
 * Исключение выбрасывается, если в сообщение не отправилось в кафку
 */
public class KafkaDeliveryException extends RuntimeException {

    public KafkaDeliveryException(String message) {
        super(message);
    }

    public KafkaDeliveryException(Throwable cause) {
        super(cause);
    }
}
