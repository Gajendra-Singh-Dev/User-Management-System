package com.gajendra.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.gajendra.entity.Attendance;
import com.gajendra.service.AttendanceService;
import com.gajendra.service.AuditLogService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AuditLogService auditLogService;

    public AttendanceController(
            AttendanceService attendanceService,
            AuditLogService auditLogService) {

        this.attendanceService = attendanceService;
        this.auditLogService = auditLogService;
    }

    // ==========================================
    // CREATE ATTENDANCE
    // ADMIN + STAFF
    // ==========================================

    @PostMapping
    public ResponseEntity<Attendance> createAttendance(
            @RequestBody Attendance attendance,
            Authentication authentication) {

        Attendance savedAttendance =
                attendanceService.createAttendance(attendance);

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "CREATE",
                "ATTENDANCE",
                "New attendance created: " + savedAttendance.getId()
        );

        return new ResponseEntity<>(
                savedAttendance,
                HttpStatus.CREATED
        );
    }

    // ==========================================
    // GET ALL ATTENDANCE
    // ADMIN + STAFF
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance()
        );
    }

    // ==========================================
    // GET ATTENDANCE BY ID
    // ADMIN + STAFF
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceById(id)
        );
    }

    // ==========================================
    // GET MY ATTENDANCE
    // STUDENT - ONLY OWN ATTENDANCE
    // ==========================================

    @GetMapping("/my")
    public ResponseEntity<List<Attendance>> getMyAttendance(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                attendanceService.getMyAttendance(email)
        );
    }

    // ==========================================
    // MY ATTENDANCE PERCENTAGE
    // ADMIN + STAFF + STUDENT
    // ==========================================

    @GetMapping("/my/percentage")
    public ResponseEntity<Double> getMyAttendancePercentage(
            Authentication authentication) {

        String email = authentication.getName();

        double percentage =
                attendanceService.getAttendancePercentage(email);

        return ResponseEntity.ok(percentage);
    }

    // ==========================================
    // UPDATE ATTENDANCE
    // ADMIN + STAFF
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(
            @PathVariable Long id,
            @RequestBody Attendance attendance,
            Authentication authentication) {

        Attendance updatedAttendance =
                attendanceService.updateAttendance(
                        id,
                        attendance
                );

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "UPDATE",
                "ATTENDANCE",
                "Attendance updated: " + updatedAttendance.getId()
        );

        return ResponseEntity.ok(updatedAttendance);
    }

    // ==========================================
    // DELETE ATTENDANCE
    // ADMIN + STAFF
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttendance(
            @PathVariable Long id,
            Authentication authentication) {

        // Delete se pehle existing attendance nikal rahe hain
        Attendance existingAttendance =
                attendanceService.getAttendanceById(id);

        // Attendance delete
        attendanceService.deleteAttendance(id);

        String userEmail = authentication.getName();

        // Audit Log
        auditLogService.createLog(
                userEmail,
                "DELETE",
                "ATTENDANCE",
                "Attendance deleted: " + existingAttendance.getId()
        );

        return ResponseEntity.ok(
                "Attendance deleted successfully"
        );
    }
}