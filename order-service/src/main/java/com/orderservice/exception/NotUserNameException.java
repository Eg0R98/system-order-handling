package com.orderservice.exception;

public class NotUserNameException extends RuntimeException {
    public NotUserNameException(String message) {
        super(message);
    }
}
