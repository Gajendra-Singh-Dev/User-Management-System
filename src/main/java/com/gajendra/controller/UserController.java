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
import com.gajendra.service.AuditLogService;
import com.gajendra.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuditLogService auditLogService;

    public UserController(
            UserService userService,
            AuditLogService auditLogService) {

        this.userService = userService;
        this.auditLogService = auditLogService;
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
            @RequestBody User user,
            Authentication authentication) {

        User savedUser =
                userService.createUser(user);

        // Logged-in admin ka email
        String adminEmail = authentication.getName();

        // Audit log create
        auditLogService.createLog(
                adminEmail,
                "CREATE",
                "USER",
                "New user created: " + savedUser.getEmail()
        );

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
            @RequestBody User user,
            Authentication authentication) {

        User updatedUser =
                userService.updateUser(id, user);

        // Logged-in admin ka email
        String adminEmail = authentication.getName();

        // Audit log create
        auditLogService.createLog(
                adminEmail,
                "UPDATE",
                "USER",
                "User updated: " + updatedUser.getEmail()
        );

        return ResponseEntity.ok(updatedUser);
    }

    // ==========================================
    // DISABLE USER
    // ADMIN ONLY
    // ==========================================

    @PutMapping("/{id}/disable")
    public ResponseEntity<User> disableUser(
            @PathVariable Long id,
            Authentication authentication) {

        User disabledUser =
                userService.disableUser(id);

        String adminEmail = authentication.getName();

        auditLogService.createLog(
                adminEmail,
                "DISABLE",
                "USER",
                "User disabled: " + disabledUser.getEmail()
        );

        return ResponseEntity.ok(disabledUser);
    }

    // ==========================================
    // ACTIVATE USER
    // ADMIN ONLY
    // ==========================================

    @PutMapping("/{id}/activate")
    public ResponseEntity<User> activateUser(
            @PathVariable Long id,
            Authentication authentication) {

        User activatedUser =
                userService.activateUser(id);

        String adminEmail = authentication.getName();

        auditLogService.createLog(
                adminEmail,
                "ACTIVATE",
                "USER",
                "User activated: " + activatedUser.getEmail()
        );

        return ResponseEntity.ok(activatedUser);
    }

    // ==========================================
    // DELETE USER
    // ADMIN ONLY
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            Authentication authentication) {

        // Delete se pehle user ko retrieve karna
        User existingUser =
                userService.getUserById(id);

        userService.deleteUser(id);

        String adminEmail = authentication.getName();

        auditLogService.createLog(
                adminEmail,
                "DELETE",
                "USER",
                "User deleted: " + existingUser.getEmail()
        );

        return ResponseEntity.ok(
                "User deleted successfully");
    }
}