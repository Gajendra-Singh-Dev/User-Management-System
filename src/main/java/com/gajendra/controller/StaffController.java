package com.gajendra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gajendra.entity.User;
import com.gajendra.service.UserService;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final UserService userService;

    public StaffController(UserService userService) {
        this.userService = userService;
    }

    // STAFF + ADMIN → User update
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {

        return ResponseEntity.ok(
                userService.updateUser(id, user)
        );
    }

    // STAFF + ADMIN → User details dekhna
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }
}