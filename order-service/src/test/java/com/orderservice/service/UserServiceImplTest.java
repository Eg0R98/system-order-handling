package com.orderservice.service;

import com.orderservice.entity.UserEntity;
import com.orderservice.exception.NotUserNameException;
import com.orderservice.repository.UserRepository;
import com.orderservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для {@link UserServiceImpl}.
 * Тестируются сценарии:
 * - создание пользователя с проверкой уникальности;
 * - получение пользователя по имени;
 * - интеграция с {@link UserDetailsService};
 * - получение текущего пользователя и его ID из Spring Security Context.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl userService;


    /**
     * Проверяет успешное создание пользователя,
     * если имя пользователя ранее не использовалось.
     */
    @Test
    void create_shouldSaveUser_whenUsernameIsUnique() {
        UserEntity user = new UserEntity();
        user.setUsername("test");

        when(repository.existsByUsername("test")).thenReturn(false);
        when(repository.save(user)).thenReturn(user);

        UserEntity result = userService.create(user);

        assertEquals(user, result);
        verify(repository, times(1)).existsByUsername("test");
        verify(repository, times(1)).save(user);
    }

    /**
     * Проверяет, что при попытке создать пользователя с занятым именем
     * выбрасывается {@link NotUserNameException}.
     */
    @Test
    void create_shouldThrowException_whenUsernameExists() {
        UserEntity user = new UserEntity();
        user.setUsername("test");

        when(repository.existsByUsername("test")).thenReturn(true);

        assertThrows(NotUserNameException.class, () -> userService.create(user));
        verify(repository, times(1)).existsByUsername("test");
        verify(repository, never()).save(any());
    }

    /**
     * Проверяет успешное получение пользователя по имени.
     */
    @Test
    void getByUsername_shouldReturnUser_whenUserExists() {
        UserEntity user = new UserEntity();
        user.setUsername("test");

        when(repository.findByUsername("test")).thenReturn(Optional.of(user));

        UserEntity result = userService.getByUsername("test");

        assertEquals(user, result);
        verify(repository, times(1)).findByUsername("test");
    }

    /**
     * Проверяет, что при отсутствии пользователя по имени
     * выбрасывается {@link UsernameNotFoundException}.
     */
    @Test
    void getByUsername_shouldThrowException_whenUserNotFound() {
        when(repository.findByUsername("test")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername("test"));
        verify(repository, times(1)).findByUsername("test");
    }

    /**
     * Проверяет, что {@link UserDetailsService} возвращает корректный сервис,
     * который делегирует вызов {@link UserServiceImpl#getByUsername(String)}.
     */
    @Test
    void userDetailsService_shouldDelegateToGetByUsername() {
        UserEntity user = new UserEntity();
        user.setUsername("test");

        when(repository.findByUsername("test")).thenReturn(Optional.of(user));

        UserDetailsService service = userService.userDetailsService();
        UserDetails result = service.loadUserByUsername("test");

        assertEquals(user, result);
    }

    /**
     * Проверяет успешное получение текущего пользователя из Spring Security Context.
     */
    @Test
    void getCurrentUser_shouldReturnAuthenticatedUser() {
        UserEntity user = new UserEntity();
        user.setUsername("current");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("current");
        when(repository.findByUsername("current")).thenReturn(Optional.of(user));

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        UserEntity result = userService.getCurrentUser();

        assertEquals(user, result);
    }

    /**
     * Проверяет успешное получение id текущего пользователя из Spring Security Context.
     */
    @Test
    void getCurrentUserId_shouldReturnUserId_whenPrincipalIsUserEntity() {
        UserEntity user = new UserEntity();
        user.setId(123L);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        Long userId = userService.getCurrentUserId();

        assertEquals(123L, userId);
    }

    /**
     * Проверяет, что при некорректном типе principal
     * выбрасывается {@link IllegalStateException}.
     */
    @Test
    void getCurrentUserId_shouldThrowException_whenPrincipalIsInvalid() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("invalid");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(IllegalStateException.class, () -> userService.getCurrentUserId());
    }
}
