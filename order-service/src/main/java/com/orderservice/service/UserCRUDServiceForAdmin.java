package com.orderservice.service;

import com.orderservice.entity.User;

import java.util.List;

public interface UserCRUDServiceForAdmin {
    List<User> findAll();

    User findById(Long id);

    User create(User user);

    User update(User user, Long id);

    void delete(Long id);
}
