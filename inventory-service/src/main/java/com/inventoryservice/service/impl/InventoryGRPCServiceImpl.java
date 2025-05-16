package com.inventoryservice.service.impl;

import com.inventoryservice.entity.Product;
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

@GrpcService
@RequiredArgsConstructor
public class InventoryGRPCServiceImpl extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final ProductRepository repository;

    @Override
    public void checkAvailability(ProductsRequest request, StreamObserver<ProductsResponse> responseObserver) {

        List<SuccessfulProductDTOFromInventoryService> successfulProducts = new ArrayList<>();
        List<UnsuccessfulProductDTOFromInventoryService> unsuccessfulProducts = new ArrayList<>();

        for (ProductDTOFromOrderService productFromRequest : request.getProductsList()) {
            Optional<Product> optionalProduct = repository.findById(UUID.fromString(productFromRequest.getProductId()));

            if(optionalProduct.isPresent() && optionalProduct.get().getQuantity() >= productFromRequest.getQuantity()){
                Product product = optionalProduct.get();
                SuccessfulProductDTOFromInventoryService item = SuccessfulProductDTOFromInventoryService.newBuilder()
                        .setProductId(String.valueOf(product.getId()))
                        .setName(product.getName())
                        .setQuantity(productFromRequest.getQuantity())
                        .setDiscountedPrice(String.valueOf(product.getDiscountedPrice()))
                        .setTotalValueWithDiscount(String.valueOf(product.getTotalValueWithDiscount()))
                        .setSale(String.valueOf(product.getSale()))
                        .build();
                successfulProducts.add(item);
            }else{
                UnsuccessfulProductDTOFromInventoryService unsuccessfulProduct =
                        UnsuccessfulProductDTOFromInventoryService.newBuilder()
                                .setProductId(productFromRequest.getProductId())
                                .setQuantity(productFromRequest.getQuantity()).build();
                unsuccessfulProducts.add(unsuccessfulProduct);
            }
        }

        ProductsResponse productsResponse = ProductsResponse.newBuilder().addAllSuccessfulProducts(successfulProducts)
                .addAllUnsuccessfulProducts(unsuccessfulProducts).build();

        responseObserver.onNext(productsResponse);
        responseObserver.onCompleted();


    }
}