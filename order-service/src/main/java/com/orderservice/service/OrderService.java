package com.orderservice.service;

import com.orderservice.dto.OrderClientDTO;
import com.orderservice.dto.OrderKafkaDTO;
import org.springframework.http.ResponseEntity;

/**
 * Сервис для обработки заказов.
 * Определяет контракт для проверки наличия товаров перед оформлением заказа.
 */
public interface OrderService {

    /**
     * Проверяет наличие товаров на складе, указанных в заказе клиента.
     * В случае отсутствия каких-либо товаров возвращает ошибку 400 (BAD REQUEST)
     * со списком недоступных товаров. В случае успеха возвращает 200 (OK) с подтверждённым списком товаров.
     *
     * @param orderClientDTO объект с деталями заказа, переданный клиентом
     * @return HTTP-ответ с результатом проверки: либо список доступных товаров, либо описание ошибки
     */
    ResponseEntity<OrderKafkaDTO> checkProducts(OrderClientDTO orderClientDTO);
}
