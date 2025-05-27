package com.notificationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Сущность заказа (OrderEntity).
 * Представляет заказ пользователя, содержит идентификатор пользователя и список товаров.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@RequiredArgsConstructor
public class OrderEntity {

    /**
     * Уникальный идентификатор заказа.
     */
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * Идентификатор пользователя, сделавшего заказ.
     * Поле уникально — у пользователя может быть только один заказ в данной таблице.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    /**
     * Список товаров, входящих в заказ.
     * Связь один-ко-многим с сущностью ProductEntity.
     */
    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    List<ProductEntity> productEntities;

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
        OrderEntity orderEntity = (OrderEntity) o;
        return getId() != null && Objects.equals(getId(), orderEntity.getId());
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
                "userId = " + userId + ")";
    }
}
