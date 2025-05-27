package com.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Сущность товара (ProductEntity).
 * Содержит информацию о товаре, который входит в заказ.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@RequiredArgsConstructor
public class ProductEntity {
    /**
     * Уникальный идентификатор товара.
     */
    @Id
    @Column(name = "id")
    private UUID id;

    /**
     * Название товара.
     */
    @Column(name = "name")
    private String name;

    /**
     * Цена товара с учётом скидки.
     */
    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    /**
     * Общая стоимость товара с учётом количества и скидки.
     */
    @Column(name = "total_value_with_discount")
    private BigDecimal totalValueWithDiscount;

    /**
     * Количество товара.
     */
    @Column(name = "quantity")
    private Integer quantity;

    /**
     * Скидка на товар в виде десятичной дроби (например, 0.10 = 10%).
     */
    @Column(name = "sale")
    private BigDecimal sale;

    /**
     * Заказ, к которому относится товар.
     * Связь многие-к-одному с сущностью OrderEntity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;


    /**
     * Переопределённый метод equals для сравнения сущностей по идентификатору.
     * Сравнивает объекты с учётом Hibernate Proxy для корректной работы с ленивой загрузкой.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны по идентификатору, иначе false
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ProductEntity productEntity = (ProductEntity) o;
        return getId() != null && Objects.equals(getId(), productEntity.getId());
    }

    /**
     * Переопределённый метод hashCode, совместимый с equals.
     * Возвращает хэш-код на основе класса сущности, учитывая Hibernate Proxy.
     *
     * @return хэш-код объекта
     */
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    /**
     * Переопределённый метод toString для удобного вывода информации об объекте.
     * Включает основные поля, полезен для логирования и отладки.
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "discountedPrice = " + discountedPrice + ", " +
                "totalValueWithDiscount = " + totalValueWithDiscount + ", " +
                "quantity = " + quantity + ", " +
                "sale = " + sale + ")";
    }
}
