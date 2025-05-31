package com.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.dto.OrderClientDTO;
import com.orderservice.dto.OrderKafkaDTO;
import com.orderservice.dto.ProductClientDTO;
import com.orderservice.exception.GlobalExceptionHandler;
import com.orderservice.exception.NotRequiredProductException;
import com.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты для {@link OrderController}, проверяющие создание заказа.
 * Используется {@link MockMvc} для имитации HTTP-запросов.
 */
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Настройка {@link MockMvc} перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }).build();
    }

    /**
     * Проверяет, что при успешном создании заказа возвращается статус 200 OK
     * и тело ответа содержит ожидаемый объект.
     */
    @Test
    void createOrder_shouldReturnOkWithOrderKafkaDTO() throws Exception {
        List<ProductClientDTO> products = List.of(ProductClientDTO.builder().build());
        OrderClientDTO orderClientDTO = OrderClientDTO.builder().products(products).build();
        OrderKafkaDTO orderKafkaDTO = new OrderKafkaDTO();

        when(orderService.checkProducts(any(OrderClientDTO.class)))
                .thenReturn(ResponseEntity.ok(orderKafkaDTO));

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderClientDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderKafkaDTO)));

        verify(orderService).checkProducts(argThat(dto ->
                dto.getProducts() != null && !dto.getProducts().isEmpty()));
        verify(orderService).checkProducts(any(OrderClientDTO.class));
    }

    /**
     * Проверяет, что при недоступности товаров возвращается статус 400 BAD REQUEST.
     */
    @Test
    void createOrder_shouldReturnBadRequestWhenProductsUnavailable() throws Exception {
        OrderClientDTO orderClientDTO = OrderClientDTO.builder().build();

        List<String> missingProducts = List.of("Product1", "Product2");

        when(orderService.checkProducts(any(OrderClientDTO.class)))
                .thenThrow(new NotRequiredProductException("Некоторые товары отсутствуют", missingProducts));

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderClientDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Некоторые товары отсутствуют")))
                .andDo(print());
        verify(orderService).checkProducts(any(OrderClientDTO.class));
    }

}
