package com.gajendra.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gajendra.dto.AuthenticationResponse;
import com.gajendra.dto.LoginRequest;
import com.gajendra.dto.RegisterRequest;
import com.gajendra.entity.Role;
import com.gajendra.entity.User;
import com.gajendra.exception.DuplicateResourceException;
import com.gajendra.repository.UserRepository;
import com.gajendra.security.JwtService;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // ==========================================
    // REGISTER
    // ==========================================

    public AuthenticationResponse register(
            RegisterRequest request) {

        // 1. Check email already exists
        if (userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already registered: "
                            + request.getEmail()
            );
        }

        // 2. Check password and confirm password
        if (!request.getPassword()
                .equals(request.getConfirmPassword())) {

            throw new RuntimeException(
                    "Password and Confirm Password do not match"
            );
        }

        // 3. Create User object
        User user = new User();

        // 4. Set name
        user.setName(request.getName());

        // 5. Set email
        user.setEmail(request.getEmail());

        // 6. Set mobile
        user.setMobile(request.getMobile());

        // 7. Encrypt password
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // 8. Default role
        user.setRole(Role.STUDENT);

        // 9. Active user
        user.setActive(true);

        // 10. Current date and time
        user.setCreatedAt(
                LocalDateTime.now()
        );

        // 11. Save user
        User savedUser =
                userRepository.save(user);

        // 12. Generate JWT Token
        String token =
                jwtService.generateToken(savedUser);

        // 13. Return response
        return new AuthenticationResponse(
                token,
                "Bearer",
                "Registration successful"
        );
    }

    // ==========================================
    // LOGIN
    // ==========================================

    public AuthenticationResponse login(
            LoginRequest request) {

        // 1. Find user by email
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        // 2. Check password
        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        // 3. Wrong password
        if (!passwordMatches) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        // 4. Generate JWT Token
        String token =
                jwtService.generateToken(user);

        // 5. Return response
        return new AuthenticationResponse(
                token,
                "Bearer",
                "Login successful"
        );
    }
}