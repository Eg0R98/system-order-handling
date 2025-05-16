package com.orderservice.service;

import com.orderservice.dto.AuthRequest;
import com.orderservice.dto.RegRequest;
import com.orderservice.security.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse registration(RegRequest request);

    JwtAuthenticationResponse authentication(AuthRequest request);

    JwtAuthenticationResponse refreshToken(String token);
}
