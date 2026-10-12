
package com.gajendra.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "login_activity")
public class LoginActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime loginDateTime;

    private String status;


    // Default Constructor
    public LoginActivity() {
    }


    // Constructor
    public LoginActivity(
            Long userId,
            LocalDateTime loginDateTime,
            String status) {

        this.userId = userId;
        this.loginDateTime = loginDateTime;
        this.status = status;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getLoginDateTime() {
        return loginDateTime;
    }

    public void setLoginDateTime(LocalDateTime loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
