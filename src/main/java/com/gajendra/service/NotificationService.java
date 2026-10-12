package com.gajendra.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gajendra.entity.Notification;
import com.gajendra.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;
    }

    // CREATE NOTIFICATION
    public Notification createNotification(
            String userEmail,
            String title,
            String message,
            String type) {

        Notification notification = new Notification();

        notification.setUserEmail(userEmail);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);

        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    // GET ALL NOTIFICATIONS OF USER
    public List<Notification> getMyNotifications(
            String userEmail) {

        return notificationRepository
                .findByUserEmailOrderByCreatedAtDesc(userEmail);
    }

    // GET UNREAD NOTIFICATIONS
    public List<Notification> getUnreadNotifications(
            String userEmail) {

        return notificationRepository
                .findByUserEmailAndIsReadFalseOrderByCreatedAtDesc(
                        userEmail
                );
    }

    // MARK AS READ
    public Notification markAsRead(
            Long id,
            String userEmail) {

        Notification notification =
                notificationRepository
                        .findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Notification not found"
                            )
                        );

        notification.setIsRead(true);

        return notificationRepository.save(notification);
    }
    // DELETE NOTIFICATION
    public void deleteNotification(
            Long id,
            String userEmail) {

        Notification notification =
                notificationRepository
                        .findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Notification not found"
                            )
                        );

        notificationRepository.delete(notification);
    }
}