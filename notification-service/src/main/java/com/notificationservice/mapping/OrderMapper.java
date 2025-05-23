package com.notificationservice.mapping;

import com.notificationservice.dto.OrderKafkaDTO;
import com.notificationservice.dto.ProductKafkaDTO;
import com.notificationservice.entity.OrderEntity;
import com.notificationservice.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper для преобразования между DTO заказов (OrderKafkaDTO) и сущностями OrderEntity и ProductEntity.
 * Использует MapStruct для автоматического маппинга, с некоторыми исключениями для вложенных сущностей.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Преобразует DTO заказа в сущность OrderEntity.
     * Свойство products игнорируется, так как для него используется отдельный маппинг.
     *
     * @param orderDTO DTO заказа
     * @return сущность OrderEntity без списка продуктов
     */
    @Mapping(target = "productEntities", ignore = true)
    OrderEntity toOrderEntity(OrderKafkaDTO orderDTO);

    /**
     * Преобразует DTO продукта в сущность ProductEntity.
     * Свойство order игнорируется, так как ссылка на заказ устанавливается вручную.
     *
     * @param dto DTO продукта
     * @return сущность ProductEntity без связи с заказом
     */
    @Mapping(target = "orderEntity", ignore = true)
    ProductEntity toProductEntity(ProductKafkaDTO dto);

    /**
     * Преобразует DTO заказа с вложенным списком продуктов в сущность OrderEntity со связными продуктами.
     * Устанавливает связь "продукт -> заказ" для корректного сохранения в БД.
     * Если список продуктов отсутствует, устанавливает пустой список.
     *
     * @param dto DTO заказа с продуктами
     * @return сущность OrderEntity с инициализированным списком продуктов
     */
    default OrderEntity mapToOrderWithProducts(OrderKafkaDTO dto) {
        OrderEntity orderEntity = toOrderEntity(dto);
        if (dto.getProducts() != null) {
        List<ProductEntity> productEntities = dto.getProducts().stream()
                .map(this::toProductEntity)
                .peek(p -> p.setOrderEntity(orderEntity))
                .toList();
        orderEntity.setProductEntities(productEntities);
        } else {
            orderEntity.setProductEntities(List.of());
        }
        return orderEntity;
    }


}
