package com.gajendra.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gajendra.dto.AuthenticationResponse;
import com.gajendra.dto.LoginRequest;
import com.gajendra.dto.RegisterRequest;
import com.gajendra.entity.User;
import com.gajendra.repository.UserRepository;
import com.gajendra.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public AuthenticationController(
            AuthenticationService authenticationService,
            UserRepository userRepository) {

        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    // ==========================================
    // GET - Show Registered Users
    // ==========================================

    @GetMapping("/register")
    public ResponseEntity<List<Map<String, Object>>> getRegisteredUsers() {

        List<User> users = userRepository.findAll();

        List<Map<String, Object>> response = users.stream()
                .map(user -> {

                    Map<String, Object> data = new LinkedHashMap<>();

                    data.put("id", user.getId());
                    data.put("name", user.getName());
                    data.put("email", user.getEmail());
                    data.put("mobile", user.getMobile());
                    data.put("role", user.getRole());
                    data.put("active", user.getActive());
                    data.put("createdAt", user.getCreatedAt());

                    return data;
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // POST - Register User
    // ==========================================

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return new ResponseEntity<>(
                authenticationService.register(request),
                HttpStatus.CREATED
        );
    }

    // ==========================================
    // POST - Login User
    // ==========================================

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authenticationService.login(request)
        );
    }
}