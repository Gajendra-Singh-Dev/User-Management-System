package com.gajendra.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gajendra.entity.Role;
import com.gajendra.entity.User;
import com.gajendra.exception.DuplicateResourceException;
import com.gajendra.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already registered: " + user.getEmail()
            );
        }

        Role role = user.getRole() == null
                ? Role.STUDENT
                : user.getRole();

        user.setRole(role);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );
    }

    public User updateUser(Long id, User user) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setMobile(user.getMobile());
        existingUser.setPassword(user.getPassword());

        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        if (user.getActive() != null) {
            existingUser.setActive(user.getActive());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );

        userRepository.delete(user);
    }
}