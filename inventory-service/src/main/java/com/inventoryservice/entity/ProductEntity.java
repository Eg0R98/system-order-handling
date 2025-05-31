package com.inventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сущность-товара
 * Представляет информацию о товаре, включая цену, количество и скидку.
 */
@Entity
@Table(name = "products")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    /**
     * Уникальный идентификатор товара.
     * Используется UUID, генерируемый автоматически.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    /**
     * Название товара.
     * Обязательное поле, не должно быть пустым.
     */
    @NotBlank
    @Column(name = "name")
    private String name;

    /**
     * Цена товара.
     * Обязательное поле, должно быть неотрицательным числом.
     */
    @NotNull
    @DecimalMin("0.0")
    @Column(name = "price")
    private BigDecimal price;

    /**
     * Количество товара на складе.
     * Значение не может быть отрицательным.
     */
    @Min(0)
    @Column(name = "quantity")
    private Integer quantity;

    /**
     * Скидка на товар в виде десятичной дроби.
     * Например, 0.10 соответствует скидке 10%.
     * Значение должно быть в диапазоне от 0.0 до 1.0.
     */
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "1.0")
    @Column(name = "sale")
    private BigDecimal sale;

    /**
     * Получение цены товара с учётом скидки.
     *
     * @return цена с применённой скидкой, если скидка отсутствует или равна нулю — возвращается исходная цена
     */
    public BigDecimal getDiscountedPrice() {
        if (sale == null || BigDecimal.ZERO.compareTo(sale) == 0) {
            return price;
        }
        return price.subtract(price.multiply(sale));
    }
      /**
     * Получение общей стоимости товара с учётом количества и скидки.
     *
     * @return общая стоимость (discountedPrice * quantity), либо 0, если количество или цена отсутствуют
     */
    public BigDecimal getTotalValueWithDiscount() {
        if (quantity == null || price == null) {
            return BigDecimal.ZERO;
        }
        return getDiscountedPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getSale() {
        return sale;
    }
}
