package com.orderservice.exception;

import java.util.List;

/**
 * Исключение выбрасывается, если в заказе имеются товары, которых нет в inventory-service
 */
public class NotRequiredProductException extends RuntimeException {

    private List<String> productsNames;

    public NotRequiredProductException(String message, List<String> productsNames) {
        super(message);
        this.productsNames = productsNames;
    }

    /**
     * Возвращает список DTO неуспешно обработанных продуктов.
     *
     * @return список продуктов, не принятых системой
     */
    public List<String> getProductsNames() {
        return productsNames;
    }

    public NotRequiredProductException(String message) {
        super(message);
    }

    public NotRequiredProductException(Throwable cause) {
        super(cause);
    }
}
