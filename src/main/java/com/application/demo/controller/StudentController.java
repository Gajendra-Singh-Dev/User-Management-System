package com.application.demo.controller;

import com.application.demo.entity.Batch;
import com.application.demo.entity.Student;
import com.application.demo.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Get student by ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // Get student's current batch
    @GetMapping("/{studentId}/batch")
    public ResponseEntity<Batch> getStudentBatch(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                studentService.getStudentBatch(studentId)
        );
    }

    // Transfer student to another batch
    @PutMapping("/{studentId}/batch/{newBatchId}")
    public ResponseEntity<Student> transferStudent(
            @PathVariable Long studentId,
            @PathVariable Long newBatchId) {

        return ResponseEntity.ok(
                studentService.transferStudentToBatch(
                        studentId,
                        newBatchId
                )
        );
    }
}