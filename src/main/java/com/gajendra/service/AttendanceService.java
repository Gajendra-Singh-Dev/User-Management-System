package com.gajendra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gajendra.entity.Attendance;
import com.gajendra.repository.AttendanceRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final NotificationService notificationService;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            NotificationService notificationService) {

        this.attendanceRepository = attendanceRepository;
        this.notificationService = notificationService;
    }

    // ==========================================
    // CREATE ATTENDANCE
    // ==========================================

    public Attendance createAttendance(
            Attendance attendance) {

        Attendance savedAttendance =
                attendanceRepository.save(attendance);

        // Automatic notification
        notificationService.createNotification(
                savedAttendance.getStudentEmail(),
                "Attendance Marked",
                "Your attendance has been marked as "
                        + savedAttendance.getStatus()
                        + " for "
                        + savedAttendance.getAttendanceDate(),
                "ATTENDANCE"
        );

        return savedAttendance;
    }

    // ==========================================
    // GET ALL ATTENDANCE
    // ==========================================

    public List<Attendance> getAllAttendance() {

        return attendanceRepository.findAll();
    }

    // ==========================================
    // GET ATTENDANCE BY ID
    // ==========================================

    public Attendance getAttendanceById(Long id) {

        return attendanceRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Attendance not found with id: " + id
                    )
                );
    }

    // ==========================================
    // GET MY ATTENDANCE
    // ==========================================

    public List<Attendance> getMyAttendance(
            String email) {

        return attendanceRepository
                .findByStudentEmail(email);
    }

    // ==========================================
    // UPDATE ATTENDANCE
    // ==========================================

    public Attendance updateAttendance(
            Long id,
            Attendance attendance) {

        Attendance existing =
                getAttendanceById(id);

        existing.setStudentId(
                attendance.getStudentId()
        );

        existing.setStudentEmail(
                attendance.getStudentEmail()
        );

        existing.setStudentName(
                attendance.getStudentName()
        );

        existing.setAttendanceDate(
                attendance.getAttendanceDate()
        );

        existing.setStatus(
                attendance.getStatus()
        );

        Attendance updatedAttendance =
                attendanceRepository.save(existing);

        // Automatic notification after update
        notificationService.createNotification(
                updatedAttendance.getStudentEmail(),
                "Attendance Updated",
                "Your attendance has been updated to "
                        + updatedAttendance.getStatus()
                        + " for "
                        + updatedAttendance.getAttendanceDate(),
                "ATTENDANCE"
        );

        return updatedAttendance;
    }

    // ==========================================
    // DELETE ATTENDANCE
    // ==========================================

    public void deleteAttendance(Long id) {

        Attendance existing =
                getAttendanceById(id);

        attendanceRepository.delete(existing);
    }

    // ==========================================
    // ATTENDANCE PERCENTAGE
    // ==========================================

    public double getAttendancePercentage(
            String email) {

        long totalAttendance =
                attendanceRepository.countByStudentEmail(email);

        long presentAttendance =
                attendanceRepository
                        .countByStudentEmailAndStatus(
                                email,
                                "PRESENT"
                        );

        if (totalAttendance == 0) {
            return 0.0;
        }

        return (presentAttendance * 100.0)
                / totalAttendance;
    }
}