
package com.gajendra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gajendra.entity.LoginActivity;
import com.gajendra.exception.ResourceNotFoundException;
import com.gajendra.repository.LoginActivityRepository;
import com.gajendra.repository.UserRepository;

@RestController
@RequestMapping("/api/login-activities")
public class LoginActivityController {

    private final LoginActivityRepository loginActivityRepository;

    private final UserRepository userRepository;


    public LoginActivityController(
            LoginActivityRepository loginActivityRepository,
            UserRepository userRepository) {

        this.loginActivityRepository = loginActivityRepository;

        this.userRepository = userRepository;
    }


    // ==========================================
    // GET ALL LOGIN ACTIVITIES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<LoginActivity>> getAllLoginActivities() {

        List<LoginActivity> activities =
                loginActivityRepository.findAll();

        return ResponseEntity.ok(activities);
    }


    // ==========================================
    // GET LOGIN HISTORY BY USER ID
    // ==========================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoginActivity>> getUserLoginHistory(
            @PathVariable Long userId) {


        // Check user exists or not
        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }


        // Get login history
        List<LoginActivity> activities =
                loginActivityRepository
                        .findByUserIdOrderByLoginDateTimeDesc(userId);


        return ResponseEntity.ok(activities);
    }
}

