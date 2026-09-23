package com.gajendra.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gajendra.dto.UserResponse;
import com.gajendra.entity.User;
import com.gajendra.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ==========================================
    // GET LOGGED-IN USER
    // ADMIN + STAFF + STUDENT
    // ==========================================

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        UserResponse response = new UserResponse(
                loggedInUser.getId(),
                loggedInUser.getName(),
                loggedInUser.getEmail(),
                loggedInUser.getMobile(),
                loggedInUser.getRole(),
                loggedInUser.getActive(),
                loggedInUser.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }
    // ==========================================
    // CREATE USER
    // ADMIN ONLY
    // ==========================================

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user) {

        User savedUser =
                userService.createUser(user);

        return new ResponseEntity<>(
                savedUser,
                HttpStatus.CREATED);
    }

    // ==========================================
    // GET ALL USERS
    // ADMIN ONLY
    // ==========================================

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers());
    }

    // ==========================================
    // GET USER BY ID
    // ADMIN ONLY
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id));
    }

    // ==========================================
    // UPDATE USER
    // ADMIN ONLY
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {

        return ResponseEntity.ok(
                userService.updateUser(id, user));
    }

    // ==========================================
    // DELETE USER
    // ADMIN ONLY
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully");
    }
}