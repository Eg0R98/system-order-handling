package com.inventoryservice.service;

import com.inventoryservice.entity.ProductEntity;
import com.inventoryservice.exception.ProductNotFoundException;
import com.inventoryservice.repository.ProductRepository;
import com.inventoryservice.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для сервиса {@link ProductServiceImpl}, отвечающего за управление товарами.
 * Используется Mockito для создания моков и проверки взаимодействий.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    /**
     * Проверяет, что метод {@code findAll} возвращает список всех товаров.
     */
    @Test
    void findAll_returnsListOfProducts() {
        List<ProductEntity> products = List.of(
                ProductEntity.builder().id(UUID.randomUUID()).name("Product1").build(),
                ProductEntity.builder().id(UUID.randomUUID()).name("Product2").build()
        );

        when(repository.findAll()).thenReturn(products);

        List<ProductEntity> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ProductEntity::getName)
                .containsExactly("Product1", "Product2");

        verify(repository).findAll();
    }

    /**
     * Проверяет, что метод {@code findById} возвращает товар, если он существует.
     */
    @Test
    void findById_whenProductExists_returnsProduct() {
        UUID id = UUID.randomUUID();
        ProductEntity product = ProductEntity.builder().id(id).name("Product").build();

        when(repository.findById(id)).thenReturn(Optional.of(product));

        ProductEntity result = service.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);

        verify(repository).findById(id);
    }

    /**
     * Проверяет, что метод {@code findById} выбрасывает {@link ProductNotFoundException},
     * если товар с указанным id не найден.
     */
    @Test
    void findById_whenProductNotFound_throwsException() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("ProductEntity with id=" + id + " not found");

        verify(repository).findById(id);
    }

    /**
     * Проверяет, что метод {@code create} сохраняет и возвращает список товаров.
     */
    @Test
    void create_savesAndReturnsProducts() {
        List<ProductEntity> productsToSave = List.of(
                ProductEntity.builder().name("NewProduct").build()
        );

        when(repository.saveAll(productsToSave)).thenReturn(productsToSave);

        List<ProductEntity> result = service.create(productsToSave);

        assertThat(result).isEqualTo(productsToSave);

        verify(repository).saveAll(productsToSave);
    }

    /**
     * Проверяет, что метод {@code delete} удаляет товар по id.
     */
    @Test
    void delete_removesProductById() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository).deleteById(id);
    }





}
