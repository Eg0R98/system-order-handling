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
 * Сущность товара (Product).
 * Содержит информацию о товаре, который входит в заказ.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@RequiredArgsConstructor
public class Product {

    /**
     * Уникальный идентификатор товара.
     */
    @Id
    private UUID id;

    /**
     * Название товара.
     */
    private String name;

    /**
     * Цена товара с учётом скидки.
     */
    private BigDecimal discountedPrice;

    /**
     * Общая стоимость товара с учётом количества и скидки.
     */
    private BigDecimal totalValueWithDiscount;

    /**
     * Количество товара.
     */
    private Integer quantity;

    /**
     * Скидка на товар в виде десятичной дроби (например, 0.10 = 10%).
     */
    private BigDecimal sale;

    /**
     * Заказ, к которому относится товар.
     * Связь многие-к-одному с сущностью Order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


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
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
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
