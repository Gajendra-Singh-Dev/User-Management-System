package com.gajendra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.AuditLog;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByCreatedAtDesc();
}