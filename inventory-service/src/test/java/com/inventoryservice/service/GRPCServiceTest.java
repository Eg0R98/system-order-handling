package com.inventoryservice.service;

import com.inventoryservice.entity.ProductEntity;
import com.inventoryservice.mapper.ProductMapper;
import com.inventoryservice.repository.ProductRepository;
import com.inventoryservice.service.impl.GRPCService;
import inventory.Product.*;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для gRPC-сервиса {@link GRPCService}, проверяющий работу метода checkAvailability.
 * Использует Mockito для мокирования зависимостей и JUnit 5 для запуска тестов.
 *
 * Проверяются следующие сценарии:
 * - Когда товар существует и его количество достаточно, возвращается успешный ответ.
 * - Когда товар не найден, возвращается неуспешный ответ.
 * - Когда товар найден, но количество недостаточно, возвращается неуспешный ответ.
 *
 * В тестах используются мок-объекты:
 * - {@link ProductRepository} — репозиторий продуктов, возвращающий данные из базы.
 * - {@link ProductMapper} — маппер для преобразования сущностей в DTO.
 * - {@link StreamObserver} — объект, через который отправляется gRPC-ответ.
 */
@ExtendWith(MockitoExtension.class)
public class GRPCServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @Mock
    private StreamObserver<ProductsResponse> responseObserver;

    @InjectMocks
    private GRPCService grpcService;

    private String productId;
    private ProductOrderServiceDTO productRequest;
    private ProductsRequest request;

    private ProductEntity productEntity;
    private SuccessfulProductInventoryServiceDTO successfulDTO;
    private UnsuccessfulProductInventoryServiceDTO unsuccessfulDTO;

    /**
     * Настраивает общие данные, которые будут использоваться в тестах:
     * - идентификатор продукта,
     * - запрос с продуктами,
     * - сущность продукта из базы,
     * - DTO для успешного и неуспешного ответа.
     */
    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID().toString();

        productRequest = ProductOrderServiceDTO.newBuilder()
                .setProductId(productId)
                .setQuantity(2)
                .build();

        request = ProductsRequest.newBuilder()
                .addProducts(productRequest)
                .build();

        productEntity = ProductEntity.builder()
                .name("Дверь")
                .id(UUID.fromString(productId))
                .quantity(5)
                .build();

        successfulDTO = SuccessfulProductInventoryServiceDTO.newBuilder()
                .setProductId(productId)
                .setQuantity(5)
                .build();

        unsuccessfulDTO = UnsuccessfulProductInventoryServiceDTO.newBuilder()
                .setName(productEntity.getName())
                .build();
    }

    /**
     * Проверяет, что если продукт найден и количество достаточное,
     * то сервис возвращает успешный ответ с этим продуктом.
     */
    @Test
    void checkAvailability_whenProductExistsAndEnoughQuantity_thenReturnsSuccessful() {
        when(repository.findById(UUID.fromString(productId))).thenReturn(Optional.of(productEntity));
        when(mapper.toSuccessfulDTO(productEntity)).thenReturn(successfulDTO);

        grpcService.checkAvailability(request, responseObserver);

        ArgumentCaptor<ProductsResponse> captor = ArgumentCaptor.forClass(ProductsResponse.class);
        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        ProductsResponse response = captor.getValue();

        assertThat(response.getSuccessfulProductsList()).contains(successfulDTO);
        assertThat(response.getUnsuccessfulProductsList()).isEmpty();
    }

    /**
     * Проверяет, что если продукт не найден,
     * то сервис возвращает неуспешный ответ.
     */
    @Test
    void checkAvailability_whenProductNotFound_thenReturnsUnsuccessful() {
        when(repository.findById(UUID.fromString(productId))).thenReturn(Optional.empty());
        when(mapper.toUnsuccessfulDTO(productRequest)).thenReturn(unsuccessfulDTO);

        grpcService.checkAvailability(request, responseObserver);

        ArgumentCaptor<ProductsResponse> captor = ArgumentCaptor.forClass(ProductsResponse.class);
        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        ProductsResponse response = captor.getValue();

        assertThat(response.getUnsuccessfulProductsList()).contains(unsuccessfulDTO);
        assertThat(response.getSuccessfulProductsList()).isEmpty();
    }

    /**
     * Проверяет, что если продукт найден, но количество недостаточно,
     * то сервис возвращает неуспешный ответ.
     */
    @Test
    void checkAvailability_whenProductExistsButNotEnoughQuantity_thenReturnsUnsuccessful() {
        ProductEntity lowQuantityProduct = ProductEntity.builder()
                .id(UUID.fromString(productId))
                .quantity(1)
                .build();

        when(repository.findById(UUID.fromString(productId))).thenReturn(Optional.of(lowQuantityProduct));
        when(mapper.toUnsuccessfulDTO(productRequest)).thenReturn(unsuccessfulDTO);

        grpcService.checkAvailability(request, responseObserver);

        ArgumentCaptor<ProductsResponse> captor = ArgumentCaptor.forClass(ProductsResponse.class);
        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        ProductsResponse response = captor.getValue();

        assertThat(response.getUnsuccessfulProductsList()).contains(unsuccessfulDTO);
        assertThat(response.getSuccessfulProductsList()).isEmpty();
    }


}
