package com.application.demo.service;

import com.application.demo.dto.BatchAttendanceSummary;
import com.application.demo.dto.StudentAttendanceSummary;
import com.application.demo.entity.Attendance;
import com.application.demo.entity.AttendanceStatus;
import com.application.demo.entity.Batch;
import com.application.demo.entity.Student;
import com.application.demo.exception.ResourceNotFoundException;
import com.application.demo.repository.AttendanceRepository;
import com.application.demo.repository.BatchRepository;
import com.application.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AttendanceReportService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final BatchRepository batchRepository;

    public AttendanceReportService(
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository,
            BatchRepository batchRepository) {

        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.batchRepository = batchRepository;
    }

    // 1. Student attendance summary
    public StudentAttendanceSummary getStudentSummary(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found: " + studentId
                        )
                );

        List<Attendance> attendanceList =
                attendanceRepository
                        .findByStudentIdOrderByAttendanceDateDesc(
                                studentId
                        );

        long totalClasses = attendanceList.size();

        long present = attendanceList.stream()
                .filter(attendance ->
                        attendance.getStatus() == AttendanceStatus.PRESENT)
                .count();

        long absent = attendanceList.stream()
                .filter(attendance ->
                        attendance.getStatus() == AttendanceStatus.ABSENT)
                .count();

        double attendancePercentage = calculatePercentage(
                present,
                totalClasses
        );

        return new StudentAttendanceSummary(
                student.getId(),
                getStudentName(student),
                totalClasses,
                present,
                absent,
                attendancePercentage
        );
    }

    // 2. Batch attendance summary
    public List<BatchAttendanceSummary> getBatchSummary(
            Long batchId) {

        // Batch validation
        batchRepository.findById(batchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Batch not found: " + batchId
                        )
                );

        // Get all students currently assigned to this batch
        List<Student> students =
                studentRepository.findByBatchId(batchId);

        // Get all attendance records of this batch
        List<Attendance> attendanceList =
                attendanceRepository.findByBatchId(batchId);

        List<BatchAttendanceSummary> result =
                new ArrayList<>();

        for (Student student : students) {

            List<Attendance> studentAttendance =
                    attendanceList.stream()
                            .filter(attendance ->
                                    attendance.getStudent()
                                            .getId()
                                            .equals(student.getId()))
                            .toList();

            long totalClasses = studentAttendance.size();

            long present = studentAttendance.stream()
                    .filter(attendance ->
                            attendance.getStatus()
                                    == AttendanceStatus.PRESENT)
                    .count();

            long absent = studentAttendance.stream()
                    .filter(attendance ->
                            attendance.getStatus()
                                    == AttendanceStatus.ABSENT)
                    .count();

            double attendancePercentage =
                    calculatePercentage(
                            present,
                            totalClasses
                    );

            result.add(
                    new BatchAttendanceSummary(
                            student.getId(),
                            getStudentName(student),
                            present,
                            absent,
                            attendancePercentage
                    )
            );
        }

        return result;
    }

    // 3. Low attendance students
    public List<BatchAttendanceSummary> getLowAttendanceStudents(
            Long batchId,
            double percentage) {

        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException(
                    "Percentage must be between 0 and 100"
            );
        }

        List<BatchAttendanceSummary> batchSummary =
                getBatchSummary(batchId);

        return batchSummary.stream()
                .filter(summary ->
                        summary.getAttendancePercentage()
                                < percentage)
                .toList();
    }

    // Percentage calculation
    private double calculatePercentage(
            long present,
            long total) {

        if (total == 0) {
            return 0.0;
        }

        double percentage =
                ((double) present / total) * 100;

        return Math.round(percentage * 100.0) / 100.0;
    }

    // Student name
    private String getStudentName(Student student) {

        return student.getStudentName();
    }
}