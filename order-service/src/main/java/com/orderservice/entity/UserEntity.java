package com.orderservice.entity;

import com.orderservice.security.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * Сущность — пользователь, на которой завязаны процессы
 * регистрации и авторизации
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "UserEntity")
public class UserEntity implements UserDetails {

    /**
     * id пользователя
     * генерируется с помощью SEQUENCE
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    /*Имя пользователя*/
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /*Пароль пользователя*/
    @Column(name = "password", nullable = false)
    private String password;

    /*Роль пользователя*/
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;


    /*Метод сообщает, какие роли есть у текущего пользователя*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    /*Возвращает true, если учётная запись не просрочена*/
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /*Возвращает true, если аккаунт не заблокирован*/
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /*Возвращает true, если учётные данные ещё действительны*/
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /*Возвращает true, если аккаунт активен*/
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    /*Переопределение equals с помощью Hibernate-прокси*/
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity userEntity = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), userEntity.getId());
    }

    /*Переопределение hashCode с помощью Hibernate-прокси*/
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
