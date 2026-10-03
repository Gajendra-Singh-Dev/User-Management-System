package com.application.demo.controller;

import com.application.demo.dto.BatchAttendanceSummary;
import com.application.demo.dto.StudentAttendanceSummary;
import com.application.demo.service.AttendanceReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceReportController {

    private final AttendanceReportService attendanceReportService;

    public AttendanceReportController(
            AttendanceReportService attendanceReportService) {
        this.attendanceReportService = attendanceReportService;
    }

    // 1. Student attendance summary
    @GetMapping("/student/{studentId}/summary")
    public ResponseEntity<StudentAttendanceSummary> getStudentSummary(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                attendanceReportService.getStudentSummary(studentId)
        );
    }

    // 2. Batch attendance summary
    @GetMapping("/batch/{batchId}/summary")
    public ResponseEntity<List<BatchAttendanceSummary>> getBatchSummary(
            @PathVariable Long batchId) {

        return ResponseEntity.ok(
                attendanceReportService.getBatchSummary(batchId)
        );
    }

    // 3. Low attendance students
    @GetMapping("/batch/{batchId}/low-attendance")
    public ResponseEntity<List<BatchAttendanceSummary>> getLowAttendanceStudents(
            @PathVariable Long batchId,
            @RequestParam double percentage) {

        return ResponseEntity.ok(
                attendanceReportService.getLowAttendanceStudents(
                        batchId,
                        percentage
                )
        );
    }
}