package com.application.demo.service;

import com.application.demo.entity.Batch;
import com.application.demo.entity.Student;
import com.application.demo.exception.ResourceNotFoundException;
import com.application.demo.repository.BatchRepository;
import com.application.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final BatchRepository batchRepository;

    public StudentService(StudentRepository studentRepository,
                          BatchRepository batchRepository) {
        this.studentRepository = studentRepository;
        this.batchRepository = batchRepository;
    }

    // Task 3: Get all students
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Task 3: Get student by ID
    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found: " + id));
    }

    // Task 4: Get all students of a particular batch
    @Transactional(readOnly = true)
    public List<Student> getStudentsByBatch(Long batchId) {

        // Check whether batch exists
        batchRepository.findById(batchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Batch not found: " + batchId));

        return studentRepository.findByBatchId(batchId);
    }

    // Task 4: Get student's current batch
    @Transactional(readOnly = true)
    public Batch getStudentBatch(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found: " + studentId));

        if (student.getBatch() == null) {
            throw new IllegalStateException(
                    "Student is not assigned to any batch: " + studentId);
        }

        return student.getBatch();
    }

    // Task 4: Transfer student to another batch
    public Student transferStudentToBatch(
            Long studentId,
            Long newBatchId) {

        // Find student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found: " + studentId));

        // Find new batch
        Batch newBatch = batchRepository.findById(newBatchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Batch not found: " + newBatchId));

        // New batch active hai ya nahi
        if (Boolean.FALSE.equals(newBatch.getActive())) {
            throw new IllegalArgumentException(
                    "Cannot transfer student to an inactive batch");
        }

        // Student already same batch me hai
        if (student.getBatch() != null
                && student.getBatch().getId().equals(newBatchId)) {

            throw new IllegalArgumentException(
                    "Student is already assigned to this batch");
        }

        // Batch capacity check
        long currentStudents =
                studentRepository.countByBatchId(newBatchId);

        Integer maxStudents = newBatch.getMaxStudents();

        if (maxStudents != null
                && currentStudents >= maxStudents) {

            throw new IllegalArgumentException(
                    "Batch capacity is full. Maximum students allowed: "
                            + maxStudents);
        }

        // Course compatibility check
        if (student.getCourse() != null
                && newBatch.getCourse() != null
                && !student.getCourse().getId()
                        .equals(newBatch.getCourse().getId())) {

            throw new IllegalArgumentException(
                    "Student cannot be transferred to a batch "
                            + "of another course");
        }

        // Transfer student
        student.setBatch(newBatch);

        return studentRepository.save(student);
    }
}