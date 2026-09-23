package com.gajendra.dto;

import java.time.LocalDateTime;

import com.gajendra.entity.Role;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private Role role;
    private Boolean active;
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public UserResponse(
            Long id,
            String name,
            String email,
            String mobile,
            Role role,
            Boolean active,
            LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}