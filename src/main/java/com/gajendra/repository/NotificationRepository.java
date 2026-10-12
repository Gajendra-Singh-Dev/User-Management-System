package com.gajendra.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.Notification;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserEmailOrderByCreatedAtDesc(
            String userEmail
    );

    List<Notification> findByUserEmailAndIsReadFalseOrderByCreatedAtDesc(
            String userEmail
    );

    Optional<Notification> findByIdAndUserEmail(
            Long id,
            String userEmail
    );
}