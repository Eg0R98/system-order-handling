package com.orderservice.service;

import com.orderservice.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
     User create(User user);

     User getByUsername(String username);

     UserDetailsService userDetailsService();

     User getCurrentUser();

     Long getCurrentUserId();

}
