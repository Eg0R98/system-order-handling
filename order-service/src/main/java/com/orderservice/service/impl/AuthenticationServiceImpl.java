package com.orderservice.service.impl;

import com.orderservice.dto.AuthRequest;
import com.orderservice.dto.RegRequest;
import com.orderservice.entity.User;
import com.orderservice.security.JwtAuthenticationResponse;
import com.orderservice.security.Role;
import com.orderservice.service.AuthenticationService;
import com.orderservice.service.JwtService;
import com.orderservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Override
    public JwtAuthenticationResponse registration(RegRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Override
    public JwtAuthenticationResponse authentication(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public JwtAuthenticationResponse refreshToken(String token) {
        String username = jwtService.extractUserName(token);

        UserDetails userDetails = userService
                .userDetailsService()
                .loadUserByUsername(username);

        if (jwtService.isTokenValid(token, userDetails)) {
            String newToken = jwtService.generateToken(userDetails);
            return new JwtAuthenticationResponse(newToken);
        }

        throw new RuntimeException("Недопустимый токен");
    }
}
