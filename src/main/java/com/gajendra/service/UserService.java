package com.gajendra.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gajendra.entity.Role;
import com.gajendra.entity.User;
import com.gajendra.exception.DuplicateResourceException;
import com.gajendra.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // CREATE USER
    // =========================
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

        // Password BCrypt encode
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    // =========================
    // GET ALL USERS
    // =========================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // =========================
    // GET USER BY ID
    // =========================
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );
    }

    // =========================
    // UPDATE USER
    // =========================
    public User updateUser(Long id, User user) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );
        
        
        //ye method put ke liye email bhi magti h 

//        existingUser.setName(user.getName());
//        existingUser.setEmail(user.getEmail());
//        existingUser.setMobile(user.getMobile());

        // =====================================
        // PASSWORD UPDATE
        // =====================================
        // Agar new password diya hai tabhi update karo
        
        if (user.getName() != null && !user.getName().isBlank()) {
            existingUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            existingUser.setEmail(user.getEmail());
        }

        if (user.getMobile() != null && !user.getMobile().isBlank()) {
            existingUser.setMobile(user.getMobile());
        }
        
        
        if (user.getPassword() != null
                && !user.getPassword().isBlank()) {

            existingUser.setPassword(
                    passwordEncoder.encode(
                            user.getPassword()
                    )
            );
        }

        // Role update
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        // Active status update
        if (user.getActive() != null) {
            existingUser.setActive(user.getActive());
        }

        return userRepository.save(existingUser);
    }

    // =========================
    // DELETE USER
    // =========================
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );

        userRepository.delete(user);
    }
    
    
    ////////////////////new user upadte desable or active 
    
    public User disableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );

        user.setActive(false);

        return userRepository.save(user);
    }


    public User activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );

        user.setActive(true);

        return userRepository.save(user);
    }
}  