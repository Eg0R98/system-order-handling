package com.orderservice.service;

import com.orderservice.dto.OrderClientDTO;
import com.orderservice.dto.OrderKafkaDTO;
import com.orderservice.exception.NotRequiredProductException;
import com.orderservice.mapping.OrderMapper;
import com.orderservice.service.impl.OrderServiceImpl;
import inventory.Product.ProductsResponse;
import inventory.Product.SuccessfulProductInventoryServiceDTO;
import inventory.Product.UnsuccessfulProductInventoryServiceDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для {@link OrderServiceImpl}.
 * Тестируется корректная обработка заказа:
 * - успешная проверка наличия товаров через gRPC, формирование Kafka-сообщения и его отправка;
 * - обработка случая, когда часть товаров недоступна, с выбрасыванием {@link NotRequiredProductException}.
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private GRPCClientService grpcClientService;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private UserService userService;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    /**
     * Проверяет успешный сценарий обработки заказа:
     * gRPC возвращает список доступных товаров, формируется {@link OrderKafkaDTO},
     * и сообщение успешно отправляется в Kafka.
     */
    @Test
    void checkProducts_shouldSendOrderToKafka_whenAllProductsAvailable() {

        OrderClientDTO clientDTO = OrderClientDTO.builder().build();
        Long userId = 3L;

        SuccessfulProductInventoryServiceDTO grpcProduct = SuccessfulProductInventoryServiceDTO.newBuilder()
                .setProductId("prod1")
                .setName("Product 1")
                .setDiscountedPrice("100.00")
                .setTotalValueWithDiscount("200.00")
                .setQuantity(2)
                .setSale("10%")
                .build();

        ProductsResponse productsResponse = ProductsResponse.newBuilder()
                .addSuccessfulProducts(grpcProduct)
                .build();

        OrderKafkaDTO orderKafkaDTO = new OrderKafkaDTO();
        orderKafkaDTO.setId(UUID.randomUUID());

        when(grpcClientService.checkAvailability(clientDTO)).thenReturn(productsResponse);
        when(userService.getCurrentUserId()).thenReturn(userId);
        when(mapper.toKafkaOrder(productsResponse.getSuccessfulProductsList(), userId)).thenReturn(orderKafkaDTO);

        ResponseEntity<OrderKafkaDTO> response = orderService.checkProducts(clientDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderKafkaDTO, response.getBody());

        verify(grpcClientService, times(1)).checkAvailability(clientDTO);
        verify(userService, times(1)).getCurrentUserId();
        verify(mapper, times(1)).toKafkaOrder(productsResponse.getSuccessfulProductsList(), userId);
        verify(kafkaProducerService, times(1)).sendOrderToKafka(orderKafkaDTO);
    }


    /**
     * Проверяет сценарий, когда gRPC возвращает список недоступных товаров.
     * В этом случае метод выбрасывает {@link NotRequiredProductException} с ожидаемым сообщением
     * и списком недоступных товаров.
     */
    @Test
    void checkProducts_shouldThrowNotRequiredProductException_whenSomeProductsUnavailable() {

        OrderClientDTO clientDTO = OrderClientDTO.builder().build();

        UnsuccessfulProductInventoryServiceDTO grpcUnavailableProduct = UnsuccessfulProductInventoryServiceDTO.newBuilder()
                .setName("Missing Product")
                .build();

        ProductsResponse productsResponse = ProductsResponse.newBuilder()
                .addUnsuccessfulProducts(grpcUnavailableProduct)
                .build();

        when(grpcClientService.checkAvailability(clientDTO)).thenReturn(productsResponse);


        NotRequiredProductException exception = assertThrows(
                NotRequiredProductException.class,
                () -> orderService.checkProducts(clientDTO)
        );

        assertEquals("Некоторые товары отсутствуют", exception.getMessage());
        assertTrue(exception.getProductsNames().contains("Missing Product"));

        verify(grpcClientService, times(1)).checkAvailability(clientDTO);
        verifyNoInteractions(userService, mapper, kafkaProducerService);
    }
}
