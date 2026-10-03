package com.application.demo.repository;

import com.application.demo.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    boolean existsByStudentIdAndAttendanceDate(
            Long studentId,
            LocalDate attendanceDate
    );

    List<Attendance> findByBatchIdAndAttendanceDate(
            Long batchId,
            LocalDate attendanceDate
    );

    List<Attendance> findByBatchId(
            Long batchId
    );

    List<Attendance> findByStudentIdOrderByAttendanceDateDesc(
            Long studentId
    );
}