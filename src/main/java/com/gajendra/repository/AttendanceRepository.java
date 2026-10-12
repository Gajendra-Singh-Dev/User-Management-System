package com.gajendra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.Attendance;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentEmail(String studentEmail);

    long countByStudentEmail(String studentEmail);

    long countByStudentEmailAndStatus(
            String studentEmail,
            String status
    );

    // Student attendance history
    List<Attendance> findByStudentIdOrderByAttendanceDateDesc(
            Long studentId
    );

    // Batch attendance records
    List<Attendance> findByBatchId(Long batchId);
}