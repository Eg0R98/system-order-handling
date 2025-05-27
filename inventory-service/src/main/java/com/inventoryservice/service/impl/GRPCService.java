package com.inventoryservice.service.impl;

import com.inventoryservice.entity.ProductEntity;
import com.inventoryservice.mapper.ProductMapper;
import com.inventoryservice.repository.ProductRepository;
import inventory.InventoryServiceGrpc;
import inventory.Product.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация gRPC сервиса InventoryService для проверки доступности товаров.
 * Использует ProductRepository для получения информации о товарах из базы данных.
 */
@GrpcService
@RequiredArgsConstructor
public class GRPCService extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    /**
     * Обрабатывает запрос на проверку доступности списка товаров.
     * Для каждого товара из запроса проверяет наличие в базе и достаточное количество.
     * Формирует ответ с двумя списками: успешно найденных товаров и отсутствующих или с недостаточным количеством.
     *
     * @param request          запрос с товарами и их количеством
     * @param responseObserver объект для отправки ответа клиенту
     */
    @Override
    public void checkAvailability(ProductsRequest request, StreamObserver<ProductsResponse> responseObserver) {

        List<SuccessfulProductInventoryServiceDTO> successfulProducts = new ArrayList<>();
        List<UnsuccessfulProductInventoryServiceDTO> unsuccessfulProducts = new ArrayList<>();

        for (ProductOrderServiceDTO productFromRequest : request.getProductsList()) {
            Optional<ProductEntity> optionalProduct = repository.findById(UUID.fromString(productFromRequest.getProductId()));

            if (optionalProduct.isPresent() && optionalProduct.get().getQuantity() >= productFromRequest.getQuantity()) {
                ProductEntity productEntity = optionalProduct.get();
                successfulProducts.add(mapper.toSuccessfulDTO(productEntity));
            } else {
                unsuccessfulProducts.add(mapper.toUnsuccessfulDTO(productFromRequest));
            }
        }

        ProductsResponse productsResponse = ProductsResponse.newBuilder()
                .addAllSuccessfulProducts(successfulProducts)
                .addAllUnsuccessfulProducts(unsuccessfulProducts)
                .build();

        responseObserver.onNext(productsResponse);
        responseObserver.onCompleted();


    }
}