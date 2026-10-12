
package com.gajendra.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gajendra.entity.Batch;
import com.gajendra.entity.Student;
import com.gajendra.exception.ResourceNotFoundException;
import com.gajendra.repository.BatchRepository;
import com.gajendra.repository.StudentRepository;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final BatchRepository batchRepository;

    public StudentService(
            StudentRepository studentRepository,
            BatchRepository batchRepository) {

        this.studentRepository = studentRepository;
        this.batchRepository = batchRepository;
    }

    // ==========================================
    // TASK 3 - GET ALL STUDENTS
    // ==========================================

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }

    // ==========================================
    // TASK 3 - GET STUDENT BY ID
    // ==========================================

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found: " + id
                        )
                );
    }

    // ==========================================
    // TASK 4 - GET STUDENTS BY BATCH
    // ==========================================

    @Transactional(readOnly = true)
    public List<Student> getStudentsByBatch(Long batchId) {

        // Check batch exists
        batchRepository.findById(batchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Batch not found: " + batchId
                        )
                );

        return studentRepository.findByBatchId(batchId);
    }

    // ==========================================
    // TASK 4 - GET STUDENT'S CURRENT BATCH
    // ==========================================

    @Transactional(readOnly = true)
    public Batch getStudentBatch(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found: " + studentId
                        )
                );

        if (student.getBatch() == null) {

            throw new IllegalStateException(
                    "Student is not assigned to any batch: "
                            + studentId
            );
        }

        return student.getBatch();
    }

    // ==========================================
    // TASK 4 - TRANSFER STUDENT TO NEW BATCH
    // ==========================================

    public Student transferStudentToBatch(
            Long studentId,
            Long newBatchId) {

        // Find student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found: " + studentId
                        )
                );

        // Find new batch
        Batch newBatch = batchRepository.findById(newBatchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Batch not found: " + newBatchId
                        )
                );

        // Check batch active
        if (Boolean.FALSE.equals(newBatch.getActive())) {

            throw new IllegalArgumentException(
                    "Cannot transfer student to an inactive batch"
            );
        }

        // Check student already in same batch
        if (student.getBatch() != null
                && student.getBatch().getId().equals(newBatchId)) {

            throw new IllegalArgumentException(
                    "Student is already assigned to this batch"
            );
        }

        // Check batch capacity
        long currentStudents =
                studentRepository.countByBatchId(newBatchId);

        Integer maxStudents =
                newBatch.getMaxStudents();

        if (maxStudents != null
                && currentStudents >= maxStudents) {

            throw new IllegalArgumentException(
                    "Batch capacity is full. "
                    + "Maximum students allowed: "
                    + maxStudents
            );
        }

        // Check course compatibility
        if (student.getCourse() != null
                && newBatch.getCourse() != null
                && !student.getCourse().getId()
                        .equals(newBatch.getCourse().getId())) {

            throw new IllegalArgumentException(
                    "Student cannot be transferred to a batch "
                    + "of another course"
            );
        }

        // Transfer student
        student.setBatch(newBatch);

        // Save student
        return studentRepository.save(student);
    }
}

