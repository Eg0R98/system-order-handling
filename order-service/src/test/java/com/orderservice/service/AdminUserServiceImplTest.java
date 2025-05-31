package com.orderservice.service;

import com.orderservice.entity.UserEntity;
import com.orderservice.repository.UserRepository;
import com.orderservice.security.Role;
import com.orderservice.service.impl.AdminUserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для {@link AdminUserServiceImpl}, проверяющие корректность работы методов управления пользователями.
 * Используется Mockito для имитации {@link UserRepository}.
 */
@ExtendWith(MockitoExtension.class)
public class AdminUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    private UserEntity testUser;

    /**
     * Подготовка данных перед каждым тестом.
     * Создает базовый тестовый {@link UserEntity}.
     */
    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser.setRole(Role.USER);
    }

    /**
     * Тестирует {@link AdminUserServiceImpl#findAll()}, проверяя, что возвращается список всех пользователей.
     */
    @Test
    void findAll_shouldReturnAllUsers() {
        List<UserEntity> userList = List.of(testUser);
        when(userRepository.findAll()).thenReturn(userList);

        List<UserEntity> result = adminUserService.findAll();

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    /**
     * Тестирует {@link AdminUserServiceImpl#findById(Long)}, когда пользователь существует.
     */
    @Test
    void findById_shouldReturnUser_whenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserEntity result = adminUserService.findById(1L);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    /**
     * Тестирует {@link AdminUserServiceImpl#findById(Long)}, проверяя выброс {@link EntityNotFoundException}, если пользователь не найден.
     */
    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> adminUserService.findById(2L));
        assertTrue(exception.getMessage().contains("UserEntity with id=2 not found"));
        verify(userRepository, times(1)).findById(2L);
    }

    /**
     * Тестирует {@link AdminUserServiceImpl#create(UserEntity)}, проверяя, что пользователь сохраняется.
     */
    @Test
    void create_shouldSaveAndReturnUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);

        UserEntity result = adminUserService.create(testUser);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    /**
     * Тестирует {@link AdminUserServiceImpl#update(UserEntity, Long)}, проверяя успешное обновление существующего пользователя.
     */
    @Test
    void update_shouldUpdateAndReturnUser_whenExists() {
        UserEntity updatedUser = new UserEntity();
        updatedUser.setUsername("newUser");
        updatedUser.setPassword("newPass");
        updatedUser.setRole(Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        UserEntity result = adminUserService.update(updatedUser, 1L);

        assertEquals("newUser", result.getUsername());
        assertEquals("newPass", result.getPassword());
        assertEquals(Role.ADMIN, result.getRole());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(testUser);
    }

    /**
     * Тестирует {@link AdminUserServiceImpl#update(UserEntity, Long)}, проверяя выброс {@link EntityNotFoundException}, если пользователь не найден.
     */
    @Test
    void update_shouldThrowException_whenUserNotFound() {
        UserEntity updatedUser = new UserEntity();
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> adminUserService.update(updatedUser, 2L));
        assertTrue(exception.getMessage().contains("UserEntity with id=2 not found"));
        verify(userRepository, times(1)).findById(2L);
        verify(userRepository, never()).save(any());
    }

    /**
     * Тестирует {@link AdminUserServiceImpl#delete(Long)}, проверяя, что метод репозитория вызван.
     */
    @Test
    void delete_shouldCallRepositoryDelete() {
        doNothing().when(userRepository).deleteById(1L);

        adminUserService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }


}
