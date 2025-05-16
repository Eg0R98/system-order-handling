package com.orderservice.repository;

import com.orderservice.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteById(@NonNull Long id);
    @NonNull
    Optional<User> findById(@NonNull Long id);
}
