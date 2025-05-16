package com.orderservice.security;

import com.orderservice.entity.User;
import com.orderservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(adminProperties.getUsername()).isEmpty()) {
            User admin = User.builder()
                    .username(adminProperties.getUsername())
                    .email(adminProperties.getEmail())
                    .password(passwordEncoder.encode(adminProperties.getPassword()))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("Администратор создан");
        } else {
            log.info("Администратор уже существует");
        }
    }
}