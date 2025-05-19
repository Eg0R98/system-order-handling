package com.notificationservice.mapping;

import com.notificationservice.dto.OrderDTOFoKafka;
import com.notificationservice.dto.ProductDTOFoKafka;
import com.notificationservice.entity.Order;
import com.notificationservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper для преобразования между DTO заказов (OrderDTOFoKafka) и сущностями Order и Product.
 * Использует MapStruct для автоматического маппинга, с некоторыми исключениями для вложенных сущностей.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Преобразует DTO заказа в сущность Order.
     * Свойство products игнорируется, так как для него используется отдельный маппинг.
     *
     * @param orderDTO DTO заказа
     * @return сущность Order без списка продуктов
     */
    @Mapping(target = "products", ignore = true)
    Order toOrderEntity(OrderDTOFoKafka orderDTO);

    /**
     * Преобразует DTO продукта в сущность Product.
     * Свойство order игнорируется, так как ссылка на заказ устанавливается вручную.
     *
     * @param dto DTO продукта
     * @return сущность Product без связи с заказом
     */
    @Mapping(target = "order", ignore = true)
    Product toProductEntity(ProductDTOFoKafka dto);

    /**
     * Преобразует DTO заказа с вложенным списком продуктов в сущность Order со связными продуктами.
     * Устанавливает связь "продукт -> заказ" для корректного сохранения в БД.
     * Если список продуктов отсутствует, устанавливает пустой список.
     *
     * @param dto DTO заказа с продуктами
     * @return сущность Order с инициализированным списком продуктов
     */
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
