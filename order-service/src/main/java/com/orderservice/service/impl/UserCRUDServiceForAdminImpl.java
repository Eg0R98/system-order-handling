package com.orderservice.service.impl;

import com.orderservice.entity.User;
import com.orderservice.repository.UserRepository;
import com.orderservice.service.UserCRUDServiceForAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCRUDServiceForAdminImpl implements UserCRUDServiceForAdmin {

    private final UserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("User with id=%d not found", id)));
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public User update(User user, Long id) {

        User userFromBD = repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("User with id=%d not found", id)));

        userFromBD.setUsername(user.getUsername());
        userFromBD.setEmail(user.getEmail());
        userFromBD.setPassword(user.getPassword());
        userFromBD.setRole(user.getRole());

        return repository.save(userFromBD);
    }

    @Override
//    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }


}
