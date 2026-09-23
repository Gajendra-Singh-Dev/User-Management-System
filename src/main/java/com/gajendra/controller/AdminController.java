package com.gajendra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gajendra.entity.Role;
import com.gajendra.entity.User;
import com.gajendra.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ADMIN → sabhi users dekh sakta hai
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                userRepository.findAll()
        );
    }

    // ADMIN → kisi user ka role change kar sakta hai
    @PutMapping("/users/{id}/role")
    public ResponseEntity<User> changeRole(
            @PathVariable Long id,
            @RequestParam Role role) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        ));

        user.setRole(role);

        return ResponseEntity.ok(
                userRepository.save(user)
        );
    }

    // ADMIN → user delete kar sakta hai
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        ));

        userRepository.delete(user);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }
}