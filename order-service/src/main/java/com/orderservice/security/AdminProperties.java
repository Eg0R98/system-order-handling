package com.orderservice.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.admin")
@Data
public class AdminProperties {
    private String username;
    private String email;
    private String password;
}