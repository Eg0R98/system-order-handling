package com.orderservice.exception;

public class KafkaDeliveryException extends RuntimeException {

    public KafkaDeliveryException(String message) {
        super(message);
    }

    public KafkaDeliveryException(Throwable cause) {
        super(cause);
    }
}
