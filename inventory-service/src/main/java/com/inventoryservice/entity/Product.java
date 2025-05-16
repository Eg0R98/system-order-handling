package com.inventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @Min(0)
    private Integer quantity;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "1.0")
    private BigDecimal sale; // скидка в виде дроби (например, 0.10 = 10%)

    // Геттер для цены со скидкой
    public BigDecimal getDiscountedPrice() {
        if (sale == null || BigDecimal.ZERO.compareTo(sale) == 0) {
            return price;
        }
        return price.subtract(price.multiply(sale));
    }

    // Геттер для общей стоимости с учетом скидки
    public BigDecimal getTotalValueWithDiscount() {
        if (quantity == null || price == null) {
            return BigDecimal.ZERO;
        }
        return getDiscountedPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
