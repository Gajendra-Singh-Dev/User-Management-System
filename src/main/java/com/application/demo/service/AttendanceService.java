package com.application.demo.service;

import com.application.demo.dto.AttendanceRequest;
import com.application.demo.entity.Attendance;
import com.application.demo.entity.Batch;
import com.application.demo.entity.Student;
import com.application.demo.exception.ResourceNotFoundException;
import com.application.demo.repository.AttendanceRepository;
import com.application.demo.repository.BatchRepository;
import com.application.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final BatchRepository batchRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository,
            BatchRepository batchRepository) {

        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.batchRepository = batchRepository;
    }

    // Mark attendance
    public Attendance markAttendance(AttendanceRequest request) {

        // 1. Student check
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found: " + request.getStudentId()
                        )
                );

        // 2. Batch check
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Batch not found: " + request.getBatchId()
                        )
                );

        // 3. Date validation
        if (request.getAttendanceDate() == null) {
            throw new IllegalArgumentException(
                    "Attendance date is required"
            );
        }

        // 4. Status validation
        if (request.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Attendance status is required"
            );
        }

        // 5. Student must belong to same batch
        if (student.getBatch() == null) {
            throw new IllegalArgumentException(
                    "Student is not assigned to any batch"
            );
        }

        if (!student.getBatch().getId().equals(batch.getId())) {
            throw new IllegalArgumentException(
                    "Student does not belong to the selected batch"
            );
        }

        // 6. Duplicate attendance check
        boolean alreadyMarked =
                attendanceRepository
                        .existsByStudentIdAndAttendanceDate(
                                student.getId(),
                                request.getAttendanceDate()
                        );

        if (alreadyMarked) {
            throw new IllegalArgumentException(
                    "Attendance already marked for this student on "
                            + request.getAttendanceDate()
            );
        }

        // 7. Create attendance
        Attendance attendance = new Attendance();

        attendance.setStudent(student);
        attendance.setBatch(batch);
        attendance.setAttendanceDate(
                request.getAttendanceDate()
        );
        attendance.setStatus(
                request.getStatus()
        );

        return attendanceRepository.save(attendance);
    }

    // Batch date-wise attendance
    @Transactional(readOnly = true)
    public List<Attendance> getBatchAttendance(
            Long batchId,
            LocalDate date) {

        // Batch existence check
        batchRepository.findById(batchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Batch not found: " + batchId
                        )
                );

        if (date == null) {
            throw new IllegalArgumentException(
                    "Date is required"
            );
        }

        return attendanceRepository
                .findByBatchIdAndAttendanceDate(
                        batchId,
                        date
                );
    }

    // Student complete attendance history
    @Transactional(readOnly = true)
    public List<Attendance> getStudentAttendance(
            Long studentId) {

        // Student existence check
        studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found: " + studentId
                        )
                );

        return attendanceRepository
                .findByStudentIdOrderByAttendanceDateDesc(
                        studentId
                );
    }
}