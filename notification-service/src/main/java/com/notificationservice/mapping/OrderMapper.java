package com.notificationservice.mapping;

import com.notificationservice.dto.OrderDTOFoKafka;
import com.notificationservice.dto.ProductDTOFoKafka;
import com.notificationservice.entity.Order;
import com.notificationservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "products", ignore = true)
    Order toOrderEntity(OrderDTOFoKafka orderDTO);

    @Mapping(target = "order", ignore = true)
    Product toProductEntity(ProductDTOFoKafka dto);

    default Order mapToOrderWithProducts(OrderDTOFoKafka dto) {
        Order order = toOrderEntity(dto);
        if (dto.getProducts() != null) {
        List<Product> products = dto.getProducts().stream()
                .map(this::toProductEntity)
                .peek(p -> p.setOrder(order))
                .toList();
        order.setProducts(products);
        } else {
            order.setProducts(List.of());
        }
        return order;
    }


}
