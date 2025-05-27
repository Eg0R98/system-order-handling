package com.inventoryservice.exception;

/**
 * Исключение выбрасывается, если товар не найден
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
